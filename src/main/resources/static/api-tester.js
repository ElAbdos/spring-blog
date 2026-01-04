const API_BASE_URL = 'http://localhost:8080';

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
        const response = await fetch(API_BASE_URL + '/articles');
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
        const response = await fetch(API_BASE_URL + '/articles/' + id);
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
        const response = await fetch(API_BASE_URL + '/articles/' + id + '/details');
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
    const authorUsername = document.getElementById('createAuthor').value;
    const content = document.getElementById('createContent').value;
    if (!authorUsername || !content) {
        alert('Veuillez remplir tous les champs');
        return;
    }
    try {
        const response = await fetch(API_BASE_URL + '/articles', {
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
        displayError(error, 400);
    }
}

async function updateArticle() {
    const id = document.getElementById('updateArticleId').value;
    const content = document.getElementById('updateContent').value;
    if (!id || !content) {
        alert('Veuillez remplir tous les champs');
        return;
    }
    try {
        const response = await fetch(API_BASE_URL + '/articles/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content })
        });
        if (!response.ok) {
            throw new Error('Erreur lors de la modification');
        }
        const data = await response.json();
        displayResponse(data, response.status);
        document.getElementById('updateArticleId').value = '';
        document.getElementById('updateContent').value = '';
    } catch (error) {
        displayError(error, 400);
    }
}

async function deleteArticle() {
    const id = document.getElementById('deleteArticleId').value;
    if (!id) {
        alert('Veuillez entrer un ID');
        return;
    }
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
        return;
    }
    try {
        const response = await fetch(API_BASE_URL + '/articles/' + id, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('Erreur lors de la suppression');
        }
        displayResponse({ message: 'Article supprimé avec succès' }, response.status);
        document.getElementById('deleteArticleId').value = '';
    } catch (error) {
        displayError(error, 400);
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
        const response = await fetch(
            API_BASE_URL + '/articles/' + articleId + '/reactions?username=' +
            encodeURIComponent(username) + '&type=' + type,
            { method: 'POST' }
        );
        if (!response.ok) {
            throw new Error('Erreur lors de l\'ajout de la réaction');
        }
        const data = await response.json();
        displayResponse(data, response.status);
    } catch (error) {
        displayError(error, 400);
    }
}

