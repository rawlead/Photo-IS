function getUsersRequest() {
    return axios.get("/api/users");
}

function getAllPhotosRequest() {
    return axios.get("/api/photos");
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

function getCategoriesRequest() {
    return axios.get("/api/categories");
}

function getCommentsToPhotoRequest(photoId) {
    return axios.get("/api/photos/" + photoId + "/comments");
}

function getFavoriteOfUsersRequst(photoId) {
    return axios.get("/api/photos/" + photoId + "/favorite");
}

function getCommentsRequest(photoId) {
    return axios.get("/api/photos/" + photoId + "/comments");
}

function getPhotosByCategoryRequest(photoCategoryId) {
    return axios.get("/api/categories/" + photoCategoryId + "/photos");
}

function getCategoryByNameRequest(photoCategoryName) {
    return axios.get("/api/categories/" + photoCategoryName);
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

function postCommentRequest(photoId, content) {
    return axios({
        method: 'post',
        url: '/api/photos/' + photoId + '/comments?' + accessToken(),
        params: {content}
    })
}

function deleteCommentRequest(userId, commentId) {
    return axios.delete("/api/users/" + userId + "/comments/" + commentId + "?" + accessToken())
}

function deletePhotoRequest(user, photo) {
    return axios.delete("/api/users/" + user.id + "/photos/" + photo.id + '?' + accessToken());
}

function getIsUserFavoriteRequest(loggedInUser, user) {
    return axios.get('/api/users/' + loggedInUser.id + '/favorite/users/' + user.id);
}

function getFavoriteUsersRequest(user) {
    return axios.get("/api/users/" + user.id + "/favorite/users")
}

function getFavoritePhotosRequest(user) {
    return axios.get("/api/users/" + user.id + "/favorite/photos")
}

function putPasswordRequest(user, oldPass, newPass, newPassConfirm) {
    return axios({
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
        MsgPop.closeAll();
        MsgPop.open({
            Type: "success",
            Content: response.data,
            CloseTimer: 1200
        });
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
        MsgPop.closeAll();
        MsgPop.open({
            Type: "warning",
            Content: response.data,
            CloseTimer: 1200
        });
    }).catch(function (error) {
        alert(error.response.data);
    }.bind(this));
}

function addPhotoToFavoritesRequest(loggedInUser, photo, object) {
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
        MsgPop.closeAll();
        MsgPop.open({
            Type: "success",
            Content: response.data,
            CloseTimer: 1200
        });
        console.log(response);
        object.fetchFavoritePhotos();
    }.bind(this)).catch(function (error) {
        console.log(error.response.data)
    });
}

function removePhotoFromFavoritesRequest(loggedInUser, photo, object) {
    axios({
        method: 'delete',
        url: '/api/users/' + loggedInUser.id + '/favorite/photos/ ' + photo.id + ' ?' + accessToken(),
    }).then(function (response) {
        MsgPop.closeAll();
        MsgPop.open({
            Type: "warning",
            Content: response.data,
            CloseTimer: 1200
        });
        object.fetchFavoritePhotos();
    }).catch(function (error) {
        alert("Something wrong with removephotofromfavorites")
    }.bind(this));
}

function signOutRequest() {
    return axios.get("/api/users/signout?" + accessToken());
}