import showModal from "./scripts/errmodal.js";
const firstName = document.getElementById("first-name");
const lastName = document.getElementById("last-name");
const signUpEmail = document.getElementById("email");
const signUpPass = document.getElementById("password");

const signUpBtn = document.getElementById("sign-up");
signUpBtn.addEventListener("click", async (_) => {
  if (
    firstName.value === "" ||
    lastName.value === "" ||
    signUpEmail.value === "" ||
    signUpPass.value === ""
  ) {
    showModal("Please do not left empty fields for sign up");
    // alert("Please do not left empty fields for registration");
    return;
  }
  if (
    !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(signUpEmail.value)
  ) {
    showModal("Please enter valid email address");
    // alert("Please enter valid email address");
    return;
  }
  try {
    let text =
      '{ "firstName":"' +
      firstName.value +
      '" , "lastName":"' +
      lastName.value +
      '" , "email":"' +
      signUpEmail.value +
      '", "password":"' +
      signUpPass.value +
      '"}';

    const obj = JSON.parse(text);
    let data = JSON.stringify(obj);
    const response = await fetch("http://localhost:8080/test/register", {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: data,
    });
    // console.log(obj);
    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      showModal(d.msg);
      // alert(d.msg);
    } else {
      showModal("Sign Up successfully");
      // alert("Sign Up successfully");
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});

const signInBtn = document.getElementById("sign-in");
const logInEmail = document.getElementById("login-email");
const LogInPass = document.getElementById("login-password");

signInBtn.addEventListener("click", async (_) => {
  try {
    let text =
      '{ "email":"' +
      logInEmail.value +
      '", "password":"' +
      LogInPass.value +
      '"}';

    const obj = JSON.parse(text);
    let data = JSON.stringify(obj);
    const response = await fetch("http://localhost:8080/test/login", {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: data,
    });

    const jsData = await response.text();

    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      showModal(d.msg);
    } else {
      // TODO  backend new pack
      // admin
      // sessionStorage.setItem("isAdmin", d.role);
      sessionStorage.setItem("isAdmin", d.role);

      sessionStorage.setItem("userID", d.uid);
      location.href = "./main.html";
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});
const resetModal = new bootstrap.Modal(
  document.getElementById("reset-password-modal"),
  {
    focus: true,
    Keyboard: true,
  }
);

const showForget = document.getElementById("forget-pwd-ling");
showForget.addEventListener("click", (e) => {
  e.preventDefault();
  if (
    !Array.from(
      document.getElementById("reset-password-modal").classList
    ).includes("show")
  ) {
    resetModal.show();
  }
});
document
  .getElementById("reset-password-modal")
  .addEventListener("show.bs.modal", () => {
    codeSuccess.style.display = "none";
    resetemail.value = "";
    verifycode.value = "";
    resetpwd.value = "";
    resetpwdconfirm.value = "";
    codeFail.style.display = "none";
  });
const sendCodeBtn = document.getElementById("sendCode-btn");
const resetBtn = document.getElementById("reset-password-btn");
const codeSuccess = document.getElementById("code-success");
const codeFail = document.getElementById("code-fail");
const verifycode = document.getElementById("verify-code");
const resetpwd = document.getElementById("exampleInputPassword1");
const resetpwdconfirm = document.getElementById("exampleInputPassword2");
const resetemail = document.getElementById("resetemail");
sendCodeBtn.addEventListener("click", async () => {
  codeSuccess.style.display = "none";
  codeFail.style.display = "none";
  if (resetemail.value == "") {
    codeSuccess.innerText = "Please enter email";
    codeSuccess.style.display = "block";
    codeSuccess.style.color = "red";
    return;
  }
  try {
    console.log(
      `http://localhost:8080/test/sendemail?email=${resetemail.value}`
    );
    const response = await fetch(
      `http://localhost:8080/test/sendemail?email=${resetemail.value}`,
      {
        method: "get",
        headers: {
          "content-type": "application/json",
        },
      }
    );

    const jsData = await response.text();
    if (jsData != "success") {
      codeSuccess.innerText = jsData;
      codeSuccess.style.display = "block";
      codeSuccess.style.color = "red";
    } else {
      codeSuccess.innerText = "success";
      codeSuccess.style.display = "block";
      codeSuccess.style.color = "darkcyan";
    }
  } catch (err) {
    showModal(err.msg);
  }
});

resetBtn.addEventListener("click", async () => {
  if (
    verifycode.value == "" ||
    resetpwd.value == "" ||
    resetpwdconfirm.value == "" ||
    resetemail.value == ""
  ) {
    codeFail.style.display = "block";
    codeFail.innerText = "Please fill all fields";
    return;
  }
  if (resetpwd.value != resetpwdconfirm.value) {
    codeFail.style.display = "block";
    codeFail.innerText = "Please enter same password";
    return;
  }
  try {
    const response = await fetch(
      `http://localhost:8080/test/resetpwd?email=${resetemail.value}&code=${verifycode.value}&password=${resetpwdconfirm.value}`,
      {
        method: "get",
        headers: {
          "content-type": "application/json",
        },
      }
    );
    const jsData = await response.text();
    if (jsData == "success") {
      resetModal.hide();
      showModal("Successfully Changed");
    }
    console.log(jsData);
  } catch (err) {
    console.log(err);
  }
});
