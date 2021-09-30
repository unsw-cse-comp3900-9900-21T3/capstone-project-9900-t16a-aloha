const firstName = document.getElementById("first-name");
const lastName = document.getElementById("last-name");
const signUpEmail = document.getElementById("email");
const signUpPass = document.getElementById("password");

const signUpBtn = document.getElementById("sign-up");
signUpBtn.addEventListener("click", async (_) => {
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
    const response = await fetch("https://httpbin.org/post", {
      method: "post",
      body: data,
    });
    // console.log(obj);
    const jsData = await response.json();

    const d = JSON.parse(jsData.data);
    console.log("Completed!", d);
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});
