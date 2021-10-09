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
    alert("Please do not left empty fields for registration");
    return;
  }
  if (
    !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(signUpEmail.value)
  ) {
    alert("Please enter valid email address");
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
      alert(d.msg);
    } else {
      alert("Sign Up successfully");
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
      alert(d.msg);
    } else {
      // TODO  backend new pack
      // admin
      // sessionStorage.setItem("isAdmin", d.role);
      sessionStorage.setItem("isAdmin", "1");

      sessionStorage.setItem("userID", d.uid);
      location.href = "./main.html";
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});
