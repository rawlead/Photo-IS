$('#search-input').attr("placeholder", " Search");

// responsive mobile nav menu
function openMobileMenu() {
    var x = document.getElementById("left-nav");
    if (x.className === "nav flex-column") {
        x.className += " responsive";
    } else {
        x.className = "nav flex-column";
    }
}

window.Event = new Vue({
    data: {isSignedIn: false}
});

function openMyProfile() {
    axios.get("/profile?access_token=" + getCookie("access_token"))
        .then(function (response) {
            document.location.replace("/profile?access_token=" + getCookie("access_token"));
        }.bind(this))
        .catch(function () {
            document.location.replace("/users/signin")
        });
}

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
    el:'#loggedUser',
    data: {
        // default avatar image
        avatar_link : '/img/user-icon-white.png',
        signedInUsername : '',
        signedIdInUserId : '',
    },
    mounted() {
        if (getCookie("access_token")) {
            axios.get("/users/loggedUser?access_token=" + getCookie("access_token"))
                .then(function (response) {
                    this.signedInUsername = response.data.username;
                    this.signedInUserId = response.data.id;
                    // if response contains avatarUrl, avatar downloaded from bucket, which url is stored in user object
                    if (response.data.avatarUrl)
                        this.avatar_link = response.data.avatarUrl;
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
            axios.get("/users/signout?access_token=" + getCookie("access_token"))
                .then(function (response) {
                    window.Event.isSignedIn = false;
                    deleteCookie("access_token");
                    window.location.replace("/");
                }.bind(this));
        },
        isSignedIn(){
            return window.Event.isSignedIn;
        }
    }
});