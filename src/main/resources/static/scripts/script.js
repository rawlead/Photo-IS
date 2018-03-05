$('#search-input').attr("placeholder", " Search");


$(function(){
    var current = location.pathname;
    $('#left-nav li a').each(function(){
        var $this = $(this);
        // if the current path is like this link, make it active
        if($this.attr('href').indexOf(current) !== -1){
            $this.addClass('active');
        }
    })
});












function addUserToFavorites(loggedInUser, user) {
    if (loggedInUser.id === user.id)
        return;
    axios({
        method: 'post',
        url: '/api/users/' + loggedInUser.id + '/favorite/users?' + accessTokenPath(),
        params: {
            favoriteUserId: user.id
        }
    }).then(function (response) {
    }.bind(this)).catch(function (error) {
        alert(error.response.data);
    });
}
function removeUserFromFavorites(loggedInUser, user) {
    if (loggedInUser.id === user.id)
        return;
    axios({
        method: 'delete',
        url: '/api/users/' + loggedInUser.id + '/favorite/users/ ' + user.id + ' ?' + accessTokenPath(),
    }).then(function (response) {
    }).catch(function (error) {
        alert(error.response.data);
    }.bind(this));
}

function addPhotoToFavorites(loggedInUser, photo) {
    if (loggedInUser.id === photo.user.id)
        return;
    console.log("Adding photo to favorites");
    axios({
        method: 'post',
        url: '/api/users/' + loggedInUser.id + '/favorite/photos?' + accessTokenPath(),
        params: {
            favoritePhotoId: photo.id
        }
    }).then(function (response) {
        console.log(response);
        author.fetchUserPhotos();
    }.bind(this)).catch(function (error) {
        console.log(error.response.data)
    });
}

function removePhotoFromFavorites(loggedInUser, photo) {
    if (loggedInUser.id === photo.user.id)
        return;
    axios({
        method: 'delete',
        url: '/api/users/' + loggedInUser.id + '/favorite/photos/ ' + photo.id + ' ?' + accessTokenPath(),
    }).then(function (response) {
        author.fetchUserPhotos();
    }).catch(function (error) {
        alert("Something wrong with removephotofromfavorites")
    }.bind(this));
}


// responsive mobile nav menu
function openMobileMenu() {
    var x = document.getElementById("left-nav");
    if (x.className === "nav flex-column") {
        x.className += " responsive";
    } else {
        x.className = "nav flex-column";
    }
}


function accessTokenPath() {
    return "access_token=" + getCookie("access_token")
}


window.Event = new Vue({
    data: {isSignedIn: false}
});

function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

function setCookie(name, value) {
    document.cookie = name + '=' + value + '; Path=/;';
}

function deleteCookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


var vueLoggedUser = new Vue({
    el: '#loggedUser',
    data: {
        avatar_link: '',
        signedInUsername: '',
        signedIdInUserId: '',
    },
    mounted() {
        if (getCookie("access_token")) {
            axios.get("/api/users/loggedUser?" + accessTokenPath())
                .then(function (response) {
                    this.signedInUsername = response.data.username;
                    this.signedInUserId = response.data.id;
                    this.avatar_link = response.data.avatarUrl;
                    // if response contains avatarUrl, avatar downloaded from bucket, which url is stored in user object
                    if (this.avatar_link === "")
                        this.avatar_link = "/img/user-icon-white.png";
                    Event.$emit('signed-in');
                    window.Event.isSignedIn = true;
                }.bind(this))
                .catch(function (error) {
                    deleteCookie("access_token");
                    return error;
                });
        }
    },
    methods: {
        signout() {
            axios.get("/api/users/signout?" + accessTokenPath())
                .then(function (response) {
                    window.Event.isSignedIn = false;
                    deleteCookie("access_token");
                    window.location.replace("/signin");
                }.bind(this));
        },
        isSignedIn() {
            return window.Event.isSignedIn;
        },
        getAvatarLink() {
            return this.avatar_link;
        }
    }
});