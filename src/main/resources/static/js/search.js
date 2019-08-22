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
            if(json[i].includes(search.value)) {
                autocomplete.innerHTML += "<span class='dropdown-item' onclick='selectData(this);'>" + json[i] + "</span>";
            }
        }
    })
    .catch(err => console.log(err));
 }

 function selectData(that) {
     var search = document.getElementById("search");
     search.value = that.innerText;

     var autocomplete = document.getElementById("autocomplete");
     autocomplete.style.display="none";
}

