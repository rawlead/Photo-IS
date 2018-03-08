function getUsersRequest() {
    return axios.get("/api/users");
}

function getUserPhotosRequest(user) {
    return axios.get("/api/users/" + user.id + "/photos");
}
function getLoggedInUserRequest() {
    return axios.get("/api/users/loggedUser?" + accessToken());
}
function getAuthorByUsernameRequest(username) {
    return axios.get("/api/users/" + username);
}


function getCommentsRequest(photoId) {
    return axios.get("/api/photos/" + photoId + "/comments")
}

function getPhotoRequest(id) {
    return axios.get("/api/photos/" + id);
}
function postPhotoRequest(id, formData) {
    return axios({
        method: 'post',
        url: '/api/users/' + id + '/photos?' + accessToken(),
        data: formData,
    })
}
function deletePhotoRequest(user,photo) {
    return axios.delete("/api/users/" + user.id + "/photos/" + photo.id + '?' + accessToken());
}
function getIsUserFavoriteRequest(loggedInUser,user) {
    return axios.get('/api/users/' + loggedInUser.id + '/favorite/users/' + user.id);
}
function getFavoriteUsersRequest(user) {
    return axios.get("/api/users/" + user.id + "/favorite/users")
}
function getFavoritePhotosRequest(user) {
    return axios.get("/api/users/" + user.id + "/favorite/photos")
}

function putPasswordRequest(user, oldPass, newPass, newPassConfirm) {
    return  axios({
        method: 'put',
        url: '/api/users/' + user.id + '/password?' + accessToken(),
        params: {oldPass, newPass, newPassConfirm}
    })
}
function putEmailRequest(user, newEmail, newEmailConfirm) {
    return axios({
        method: 'put',
        url: '/api/users/' + user.id + '/email?' + accessToken(),
        params: {newEmail, newEmailConfirm}
    })
}
function putAvatarRequest(user, formData) {
    return axios.put('/api/users/' + user.id + '/avatar?' + accessToken(), formData);
}

function addUserToFavoritesRequest(loggedInUser, user) {
    if (loggedInUser === '') {
        window.location.replace("/signin");
        return;
    }
    if (loggedInUser.id === user.id)
        return;
    axios({
        method: 'post',
        url: '/api/users/' + loggedInUser.id + '/favorite/users?' + accessToken(),
        params: {
            favoriteUserId: user.id
        }
    }).then(function (response) {
    }.bind(this)).catch(function (error) {
        alert(error.response);
    });
}
function removeUserFromFavoritesRequest(loggedInUser, user) {
    if (loggedInUser.id === user.id)
        return;
    axios({
        method: 'delete',
        url: '/api/users/' + loggedInUser.id + '/favorite/users/ ' + user.id + ' ?' + accessToken(),
    }).then(function (response) {
    }).catch(function (error) {
        alert(error.response.data);
    }.bind(this));
}
function addPhotoToFavoritesRequest(loggedInUser, photo) {
    if (loggedInUser === '') {
        window.location.replace("/signin");
        return;
    }
    axios({
        method: 'post',
        url: '/api/users/' + loggedInUser.id + '/favorite/photos?' + accessToken(),
        params: {
            favoritePhotoId: photo.id
        }
    }).then(function (response) {
        console.log(response);
        author.fetchFavoritePhotos();
    }.bind(this)).catch(function (error) {
        console.log(error.response.data)
    });
}
function removePhotoFromFavoritesRequest(loggedInUser, photo) {
    axios({
        method: 'delete',
        url: '/api/users/' + loggedInUser.id + '/favorite/photos/ ' + photo.id + ' ?' + accessToken(),
    }).then(function (response) {
        author.fetchFavoritePhotos();
    }).catch(function (error) {
        alert("Something wrong with removephotofromfavorites")
    }.bind(this));
}
function signOutRequest() {
    return axios.get("/api/users/signout?" + accessToken());

}