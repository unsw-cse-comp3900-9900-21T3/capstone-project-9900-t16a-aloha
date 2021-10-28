const zoomBtn = document.getElementById("access-zoom-btn");

const loadMoreBtns = document.getElementsByClassName("load-more-btns");
const productCards = document.getElementsByClassName("product-card");
const productDetailText = document.getElementsByClassName("products-text");
const userSearchPage = document.getElementsByClassName("mag-to-fluid");
const cursor = document.querySelector(".cursor");
zoomBtn.addEventListener("click", () => {
  document.getElementsByTagName("HTML")[0].classList.toggle("zoomin");
  cursor.classList.toggle("zoomin");
  for (let lmBtn of loadMoreBtns) {
    lmBtn.classList.toggle("zoomin");
  }
  for (let card of productCards) {
    card.classList.toggle("zoomin");
  }
  for (let pText of productDetailText) {
    pText.classList.toggle("zoomin");
  }
  for (let page of userSearchPage) {
    page.classList.toggle("container");
    page.classList.toggle("container-fluid");
  }
});

const eyeCareBtns = document.getElementsByClassName("eye-care-btns");
for (let eBtn of eyeCareBtns) {
  eBtn.addEventListener("click", () => {
    if (eBtn.classList.toggle("eye-care-on")) {
      const bg = eBtn.style.backgroundColor;
      document.body.style.backgroundColor = bg;
      for (let ele of document.getElementsByClassName("offcanvas")) {
        ele.style.backgroundColor = bg;
      }
      for (let pic of document.getElementsByTagName("img")) {
        pic.style.filter = "sepia(10%)";
      }
    } else {
      document.body.style.backgroundColor = "initial";
      for (let ele of document.getElementsByClassName("offcanvas")) {
        ele.style.backgroundColor = "white";
      }
      for (let pic of document.getElementsByTagName("img")) {
        pic.style.filter = "";
      }
    }
  });
}

document.addEventListener("mousemove", (e) => {
  cursor.setAttribute(
    "style",
    "top: " + (e.pageY - 25) + "px; left: " + (e.pageX - 21) + "px;"
  );
});

document.addEventListener("click", () => {
  cursor.classList.add("expand");

  setTimeout(() => {
    cursor.classList.remove("expand");
  }, 500);
});
