(() => {
    const YEAR_BEGIN = 1900

    const target = document.getElementById("birth-year")
    for (let i = (new Date()).getFullYear(); i >= YEAR_BEGIN ; i--) {
        const year = document.createElement("option")
        year.setAttribute("value", i)
        year.innerHTML = i
        target.appendChild(year)
    }

    document.getElementById("registration-form").addEventListener("submit", event => {
        if (document.getElementById("new-password").value != document.getElementById("new-password-confirm").value) {
            alert("비밀번호가 일치하지 않습니다. 다시 확인하여 주시기 바랍니다.")
            event.preventDefault()
        } else {
            document.getElementById("birth").value = document.getElementById("birth-year").value + "/" +
                                                    document.getElementById("birth-month").value + "/" +
                                                    document.getElementById("birth-day").value
        }
    })
})()