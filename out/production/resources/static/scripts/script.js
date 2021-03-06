$('#search-input').attr("placeholder", " search");

// Lazy load
Vue.use(VueLazyload, {
    preLoad: 1.3,
    loading: '/img/placeholder-image.png',
    attempt: 1
});

// Photo grid VueJS Masonry library
Vue.use(VueMasonry);

// highlight active left nav link
$(function () {
    let current = location.pathname;
    $('#left-nav').find('li a').each(function () {
        let $this = $(this);
        if ($this.attr('href').indexOf(current) !== -1) {
            $this.addClass('active');
        }
    })
});

// responsive mobile nav menu
function openMobileMenu() {
    let x = document.getElementById("left-nav");
    if (x.className === "nav flex-column") {
        x.className += " responsive";
    } else {
        x.className = "nav flex-column";
    }
}

function openCategory(categoryName) {
    localStorage.setItem("categoryName", categoryName);
    window.location.replace("/photos")
}

function accessToken() {
    return "access_token=" + getCookie("access_token")
}

window.Event = new Vue({
    data: {isSignedIn: false}
});

function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}

//
function setCookie(name, value) {
    document.cookie = name + '=' + value + '; Path=/;';
}

function deleteCookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

var vueLoggedUser = new Vue({
    el: '#topNavRoot',
    data: {
        avatar_link: '',
        user: '',
        users: [],
        categories: [],
        search: '',
        category: ''
    },
    mounted() {
        this.fetchUsers();
        this.fetchCategories();
        this.fetchLoggedInUser();
    },
    computed: {
        filteredUsers() {
            return this.users.filter(user => {
                return (user.firstName.toLowerCase().includes(this.search.toLowerCase())
                    || user.username.toLowerCase().includes(this.search.toLowerCase()))
                    && this.search !== '';
            })
        },
        filteredCategories() {
            return this.categories.filter(category => {
                return category.name.toLowerCase().includes(this.search.toLowerCase())
                    && this.search !== '';
            })
        }
    },
    methods: {
        fetchLoggedInUser() {
            if (getCookie("access_token")) {
                getLoggedInUserRequest()
                    .then(function (response) {
                        this.user = response.data;
                        // if response contains avatarUrl, avatar downloaded from bucket, which url is stored in user object
                        if (this.avatar_link === "")
                            this.avatar_link = "/img/user-icon-white.png";
                        Event.$emit('signed-in');
                        window.Event.isSignedIn = true;
                        return response.data;
                    }.bind(this))
                    .catch(function (error) {
                        deleteCookie("access_token");
                        window.location.replace("/signin");
                    });
            }
        },
        fetchUsers() {
            getUsersRequest()
                .then(function (response) {
                    this.users = response.data;
                }.bind(this))
        },
        fetchCategories() {
            getCategoriesRequest()
                .then(function (response) {
                    this.categories = response.data;
                }.bind(this))
        },
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
        },
        openCategory(name) {
            openCategory(name);
        }
    },
});

function signin(username, password) {
    var params = new URLSearchParams();
    params.append('grant_type', 'password');
    params.append('username', username);
    params.append('password', password);
    axios({
        method: 'post',
        url:'/oauth/token',
        auth:{
            username:'photois-client',password:'photois-secret'
        },
        headers: {"Content-type": "application/x-www-form-urlencoded; charset=utf-8"},
        data:params
    }).then(function (response) {
        setCookie("access_token", response.data.access_token);
        document.location.replace("/profile");
    }).catch(function (error) {
        signinVue.statusMessage = "Wrong username or password";
        signinVue.showAndHideAlert();
    }.bind(this))
}


/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function toggleSearch(event) {
    // document.getElementById("search-dropdown").classList.toggle("showSearch");
    $("#search-dropdown").toggleClass( "showSearch" );
    // $("#search-dropdown").css("display", "block");
    // $("#search-dropdown").css("width", "40%");
}

$(document).mouseup(function (e) {
    var container = $("#search-dropdown");
    if (!container.is(e.target) && container.has(e.target).length === 0)
    container.removeClass('showSearch')
    //     $("#search-dropdown").css("display", "none");
    //     container.css("width", "0");
});

function deleteUser(userId) {
    if (vueLoggedUser.user.id === userId) {
        alert("Cannot delete admin user");
        return;
    }
    deleteUserRequest(userId)
        .then(function () {
            window.location.replace("/authors");
        })
}