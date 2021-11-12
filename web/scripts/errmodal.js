const showModal = (msg) => {
  if (
    !Array.from(document.getElementById("err-modal").classList).includes("show")
  ) {
    const errModal = new bootstrap.Modal(document.getElementById("err-modal"), {
      focus: true,
      Keyboard: true,
    });
    const errMsg = document.getElementById("err-modal-msg");
    errMsg.innerText = msg;
    errModal.show();
  }
};
export default showModal;
