$('#search-input').attr("placeholder", " Search");
loadProgressBar();


// highlight active left nav link
$(function () {
    var current = location.pathname;
    $('#left-nav li a').each(function () {
        var $this = $(this);
        if ($this.attr('href').indexOf(current) !== -1) {
            $this.addClass('active');
        }
    })
});

// responsive mobile nav menu
function openMobileMenu() {
    var x = document.getElementById("left-nav");
    if (x.className === "nav flex-column") {
        x.className += " responsive";
    } else {
        x.className = "nav flex-column";
    }
}


function accessToken() {
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
        user: '',
    },
    mounted() {
        if (getCookie("access_token")) {
            getLoggedInUserRequest()
                .then(function (response) {
                    this.user = response.data;
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
                    window.location.replace("/signin");
                    return error;
                });
        }
    },
    methods: {
        signout() {
            signOutRequest()
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