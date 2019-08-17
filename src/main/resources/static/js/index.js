const validateRegistrationForm = () => {
    if (document.getElementById("new-password").value != document.getElementById("new-password-confirm").value) {
        alert("비밀번호를 다시 확인하여 주시기 바랍니다.")
        return false
    }
    document.getElementById("name").value = document.getElementById("lastname").value + document.getElementById("firstname").value
    document.getElementById("birth").value = document.getElementById("birth-year").value + "/" +
                                            document.getElementById("birth-month").value + "/" +
                                            document.getElementById("birth-day").value
    return true
}