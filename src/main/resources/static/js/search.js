const search = function(){
    const SPACE_PATTERN = /\s/g;

    let search = document.getElementById("search");
    let autocomplete = document.getElementById("autocomplete");
    let keyword = "";

    search.onkeyup = function(e){
       keyword = search.value;

       if ( keyword.match(SPACE_PATTERN) || keyword == "" ) {
          autocomplete.style.display = "none";
          autocomplete.innerHTML = "";
       }
       else {
          console.log(keyword);
          autocomplete.style.display = "block";
          findUserNamesByKeyword(autocomplete, keyword);
       }
    };
 }();

 function findUserNamesByKeyword(autocomplete, keyword) {
    const BASE_URL = `http://${window.location.host}`

    fetch(BASE_URL + "/api/users/" + keyword, {
        method : "GET",
        headers: {
           "Accept": "application/json"
        }
    })
    .then(res => {
        console.log(res);
        return res.json()})
    .then(json => {
        autocomplete.innerHTML = "";
        console.log(json);
        for (var i=0; i < json.length; i++) {
            autocomplete.innerHTML += "<span class='dropdown-item' onclick='selectData(" + JSON.stringify(json[i]) +");'>" + json[i].name + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                        + "<span style='color:grey;'>" + json[i].email + "</span>" + "</span>";
        }
    })
    .catch(err => console.log(err));
 }

 function selectData(json) {
     console.log(json);
     let search = document.getElementById("search");
     search.value = json.name;

     let autocomplete = document.getElementById("autocomplete");
     autocomplete.style.display="none";
     let searchform = document.getElementById('searchform');
     searchform.setAttribute("action", "/users/" + json.id);
}

