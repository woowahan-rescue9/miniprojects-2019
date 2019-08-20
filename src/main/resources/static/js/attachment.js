const attachmentModal = document.getElementById('attachment-modal')
const attachmentOpen = document.getElementById('attachment-open')
const attachmentClose = document.getElementById('attachment-close')

attachmentOpen.onclick = function () {
    attachmentModal.style.display = "block"
}

attachmentClose.onclick = function () {
    attachmentModal.style.display = "none"
}

window.onclick = function (event) {
    if (event.target === attachmentModal) {
        attachmentModal.style.display = "none"
    }
}