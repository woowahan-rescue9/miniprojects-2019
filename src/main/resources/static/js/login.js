 const login = function login() {
    const BASE_URL = `http://${window.location.host}`

    document.getElementById("login-button").addEventListener("click", function () {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        const loginData = {
            email: email,
            password: password
        };

        fetch(BASE_URL + "/api/login", {
                method : "POST",
                headers: {
                   "Content-Type": "application/json; charset=utf-8"
                },
                body: JSON.stringify(loginData)
        })
        .then(response => {
            if(response.status === 200){
                alert('로그인 완료')
                window.location.href = BASE_URL + '/newsfeed';
            }
            else{
                response.text().then(text => alert(text));
            }
        });
 })}();
