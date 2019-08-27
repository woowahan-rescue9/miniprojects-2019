 const signup = function signup() {
     const YEAR_BEGIN = 1900

     const target = document.getElementById("birth-year")
     for (let i = (new Date()).getFullYear(); i >= YEAR_BEGIN ; i--) {
         const year = document.createElement("option")
         year.setAttribute("value", i)
         year.innerHTML = i
         target.appendChild(year)
     }

    const BASE_URL = `http://${window.location.host}`

    document.getElementById("registration-button").addEventListener("click", function () {
        if (document.getElementById("new-password").value != document.getElementById("new-password-confirm").value) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }

        const lastname = document.getElementById('lastname').value;
        const firstname = document.getElementById('firstname').value;
        const email = document.getElementById('new-email').value;
        const password = document.getElementById('new-password').value;
        const gender = document.getElementById('female').checked ? 'female' : 'male';
        const birth = document.getElementById("birth-year").value + "/" +
                                                    document.getElementById("birth-month").value + "/" +
                                                    document.getElementById("birth-day").value;
        const signupData = {
            email: email,
            lastName: lastname,
            firstName: firstname,
            password: password,
            gender: gender,
            birth: birth
        };

        fetch(BASE_URL + "/api/users", {
                method : "POST",
                headers: {
                   "Content-Type": "application/json; charset=utf-8"
                },
                body: JSON.stringify(signupData)
        })
        .then(response => {
            if(response.status === 201){
                alert('회원가입 완료')
                window.location.href = BASE_URL + '/newsfeed';
            }
            else{
                response.text().then(text => alert(text));
            }
        });
 })}();
