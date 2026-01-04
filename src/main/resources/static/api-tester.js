const API_BASE_URL = 'http://localhost:8080';
let authToken = null;
let currentUsername = null;


function switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
    if (tab === 'login') {
        document.querySelectorAll('.auth-tab')[0].classList.add('active');
        document.getElementById('loginForm').classList.add('active');
    } else {
        document.querySelectorAll('.auth-tab')[1].classList.add('active');
        document.getElementById('registerForm').classList.add('active');
    }
}

function performLogin() {
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value.trim();

    if (!username || !password) {
        showMessage('loginMessage', 'Veuillez entrer un nom d\'utilisateur et un mot de passe', 'error');
        return;
    }

    fetch(API_BASE_URL + '/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Identifiants invalides');
        }
        return response.json();
    })
    .then(data => {
        authToken = data.token;
        currentUsername = username;
        document.getElementById('loginForm').style.display = 'none';
        document.getElementById('registerForm').style.display = 'none';
        document.querySelector('.auth-tabs').style.display = 'none';
        document.getElementById('authInfo').style.display = 'block';
        document.getElementById('currentUser').textContent = username;
        document.getElementById('tokenDisplay').textContent = authToken;

        showMessage('loginMessage', 'Connexion réussie', 'success');

        setTimeout(() => {
            document.getElementById('loginMessage').style.display = 'none';
        }, 3000);
    })
    .catch(error => {
        showMessage('loginMessage', 'Erreur : ' + error.message, 'error');
    });
}

function performRegister() {
    const username = document.getElementById('registerUsername').value.trim();
    const password = document.getElementById('registerPassword').value.trim();
    const role = document.getElementById('registerRole').value;

    if (!username || !password) {
        showMessage('registerMessage', 'Veuillez remplir tous les champs', 'error');
        return;
    }

    if (username.length < 3) {
        showMessage('registerMessage', 'Le nom d\'utilisateur doit contenir au moins 3 caractères', 'error');
        return;
    }

    if (password.length < 6) {
        showMessage('registerMessage', 'Le mot de passe doit contenir au moins 6 caractères', 'error');
        return;
    }

    fetch(API_BASE_URL + '/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, role })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.error || 'Erreur lors de l\'inscription');
            });
        }
        return response.json();
    })
    .then(data => {
        showMessage('registerMessage', 'Inscription réussie ! Vous pouvez maintenant vous connecter.', 'success');

        document.getElementById('registerUsername').value = '';
        document.getElementById('registerPassword').value = '';

        setTimeout(() => {
            switchAuthTab('login');
            document.getElementById('loginUsername').value = username;
        }, 2000);
    })
    .catch(error => {
        showMessage('registerMessage', 'Erreur : ' + error.message, 'error');
    });
}

function performLogout() {
    authToken = null;
    currentUsername = null;

    document.getElementById('loginForm').style.display = 'block';
    document.querySelector('.auth-tabs').style.display = 'flex';
    document.getElementById('authInfo').style.display = 'none';
    document.getElementById('loginPassword').value = '';

    switchAuthTab('login');
    showMessage('loginMessage', 'Déconnexion réussie', 'success');

    setTimeout(() => {
        document.getElementById('loginMessage').style.display = 'none';
    }, 2000);
}

function showMessage(elementId, message, type) {
    const messageDiv = document.getElementById(elementId);
    messageDiv.textContent = message;
    messageDiv.className = 'auth-message ' + type;
    messageDiv.style.display = 'block';
}

function fetchWithAuth(url, options = {}) {
    if (authToken) {
        options.headers = options.headers || {};
        options.headers['Authorization'] = 'Bearer ' + authToken;
    }
    return fetch(url, options);
}
function displayResponse(data, status, isError = false) {
    const responseDiv = document.getElementById('response');
    const statusClass = isError ? 'error' : 'success';
    const statusBadgeClass = isError ? 'error' : 'success';

    responseDiv.className = 'response-box ' + statusClass;
    responseDiv.innerHTML =
        '<span class="status-badge ' + statusBadgeClass + '">HTTP ' + status + '</span>' +
        '<pre>' + JSON.stringify(data, null, 2) + '</pre>';

    responseDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

function displayError(error, status = 'ERROR') {
    console.error('Erreur:', error);
    displayResponse({
        error: error.message || 'Une erreur est survenue',
        details: error.toString()
    }, status, true);
}

async function listArticles() {
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles');
        const data = await response.json();
        displayResponse(data, response.status);
    } catch (error) {
        displayError(error);
    }
}

async function getArticle() {
    const id = document.getElementById('getArticleId').value;
    if (!id) {
        alert('Veuillez entrer un ID');
        return;
    }
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles/' + id);
        if (!response.ok) {
            throw new Error('Article non trouvé');
        }
        const data = await response.json();
        displayResponse(data, response.status);
    } catch (error) {
        displayError(error, 404);
    }
}

async function getArticleDetails() {
    const id = document.getElementById('getDetailsId').value;
    if (!id) {
        alert('Veuillez entrer un ID');
        return;
    }
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles/' + id + '/details');
        if (!response.ok) {
            throw new Error('Article non trouvé');
        }
        const data = await response.json();
        displayResponse(data, response.status);
    } catch (error) {
        displayError(error, 404);
    }
}

async function createArticle() {
    if (!authToken) {
        alert('Vous devez être connecté pour créer un article !');
        return;
    }

    const authorUsername = document.getElementById('createAuthor').value;
    const content = document.getElementById('createContent').value;
    if (!authorUsername || !content) {
        alert('Veuillez remplir tous les champs');
        return;
    }
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ authorUsername, content })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erreur lors de la création');
        }
        const data = await response.json();
        displayResponse(data, response.status);
        document.getElementById('createAuthor').value = '';
        document.getElementById('createContent').value = '';
    } catch (error) {
        displayError(error, 403);
    }
}

async function updateArticle() {
    if (!authToken) {
        alert('Vous devez être connecté pour modifier un article !');
        return;
    }

    const id = document.getElementById('updateArticleId').value;
    const content = document.getElementById('updateContent').value;
    if (!id || !content) {
        alert('Veuillez remplir tous les champs');
        return;
    }
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erreur lors de la modification');
        }
        const data = await response.json();
        displayResponse(data, response.status);
        document.getElementById('updateArticleId').value = '';
        document.getElementById('updateContent').value = '';
    } catch (error) {
        displayError(error, 403);
    }
}

async function deleteArticle() {
    if (!authToken) {
        alert('Vous devez être connecté pour supprimer un article !');
        return;
    }

    const id = document.getElementById('deleteArticleId').value;
    if (!id) {
        alert('Veuillez entrer un ID');
        return;
    }
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
        return;
    }
    try {
        const response = await fetchWithAuth(API_BASE_URL + '/articles/' + id, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('❌ Error response:', errorText);
            throw new Error(errorText || 'Erreur lors de la suppression');
        }
        displayResponse({ message: 'Article supprimé avec succès' }, response.status);
        document.getElementById('deleteArticleId').value = '';
    } catch (error) {
        console.error('❌ Exception caught:', error);
        displayError(error, 403);
    }
}

async function addReaction() {

    const articleId = document.getElementById('reactionArticleId').value;
    const username = document.getElementById('reactionUsername').value;
    const type = document.getElementById('reactionType').value;
    if (!articleId || !username) {
        alert('Veuillez remplir tous les champs');
        return;
    }
    try {
        const response = await fetchWithAuth(
            API_BASE_URL + '/articles/' + articleId + '/reactions?username=' +
            encodeURIComponent(username) + '&type=' + type,
            { method: 'POST' }
        );
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erreur lors de l\'ajout de la réaction');
        }
        const data = await response.json();
        displayResponse(data, response.status);
    } catch (error) {
        displayError(error, 403);
    }
}

