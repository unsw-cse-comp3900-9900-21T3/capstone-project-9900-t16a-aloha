const zoomBtn = document.getElementById("access-zoom-btn");

const loadMoreBtns = document.getElementsByClassName("load-more-btns");
const productCards = document.getElementsByClassName("product-card");
const productDetailText = document.getElementsByClassName("products-text");
const userSearchPage = document.getElementsByClassName("mag-to-fluid");

zoomBtn.addEventListener("click", () => {
  document.getElementsByTagName("HTML")[0].classList.toggle("zoomin");
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
    console.log("???");
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
        ele.style.backgroundColor = "initial";
      }
      for (let pic of document.getElementsByTagName("img")) {
        pic.style.filter = "";
      }
    }
  });
}
