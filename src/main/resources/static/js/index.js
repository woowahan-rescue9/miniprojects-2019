const birthdateInit = () => {
    const YEAR_BEGIN = 1900

    const target = document.getElementById("birth-year")
    for (let i = (new Date()).getFullYear(); i > YEAR_BEGIN ; i--) {
        const year = document.createElement("option")
        year.setAttribute("value", i)
        year.innerHTML = i
        target.appendChild(year)
    }   
}

birthdateInit()

const validateRegistrationForm = () => {
    if (document.getElementById("new-password").value != document.getElementById("new-password-confirm").value) {
        alert("비밀번호를 다시 확인하여 주시기 바랍니다.")
        return false
    }
    document.getElementById("birth").value = document.getElementById("birth-year").value + "/" +
                                            document.getElementById("birth-month").value + "/" +
                                            document.getElementById("birth-day").value
    return true
}