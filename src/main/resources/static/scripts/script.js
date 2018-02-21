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

function getLoggedInUsername() {
    if (getCookie("access_token")) {
        axios.get("/getUsername?access_token=" + getCookie("access_token"))
            .then(function (response) {
                return response.data
            });
    }
}

var vueLoggedUser = new Vue({
    el:'#loggedUser',
    data: {logged_message : "", },
        // isSignedIn : false},
    mounted() {
        if (getCookie("access_token")) {
            axios.get("/getUsername?access_token=" + getCookie("access_token"))
                .then(function (response) {
                    this.logged_message = "Hello, " + response.data;
                    Event.$emit('signed-in');
                    // this.isSignedIn = true;
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
                    // this.isSignedIn = false;
                    window.Event.isSignedIn = false;
                    deleteCookie("access_token");
                    window.location.replace("/");
                    // this.logged_message = "Good logged out";
                }.bind(this));
        },
        isSignedIn(){
            return window.Event.isSignedIn;
        }
    }
});

var store = {
    state: {
        message: 'WORKS',
    },

    // state: {
    //     message: 'Hello!'
    // },
    setMessage (newValue) {
        this.state.message = newValue;

    },
    // clearMessageAction () {
    //     if (this.debug) console.log('clearMessageAction triggered')
    //     this.state.message = ''
    // }
};
