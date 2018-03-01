$(document).ready(function(){ 
  document.getElementsByClassName('profileTabBtn')[0].click()
});


var currentProfileTab = '';


function openProfileTab(evt, tabName) {
    var i, profileTabContent, profileTabBtn;
    profileTabContent = document.getElementsByClassName("profileTabContent");
    for (i = 0; i < profileTabContent.length; i++) {
        profileTabContent[i].style.display = "none";
    }
    profileTabBtn = document.getElementsByClassName("profileTabBtn");
    for (i = 0; i < profileTabBtn.length; i++) {
        profileTabBtn[i].className = profileTabBtn[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "flex";
    console.log("tab name " + tabName);
    evt.currentTarget.className += " active";
    currentProfileTab = tabName;
}
