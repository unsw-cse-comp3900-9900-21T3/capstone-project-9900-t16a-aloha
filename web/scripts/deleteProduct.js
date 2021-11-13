import { getProductList } from "./adminProductList.js";
import showModal from "./errmodal.js";
const deleteBtns = document.getElementsByClassName(
  "product-list-page-card-delete"
);
const myModal = document.getElementById("delete-product-confirmation");
// const deleteProduct =
myModal.addEventListener("shown.bs.modal", (e) => {
  const idStr = e.relatedTarget.id;
  console.log(idStr.substring(8, idStr.length));
  sessionStorage.setItem("toDel", idStr.substring(8, idStr.length));
});

// for (let btn of deleteBtns) {
//   btn.addEventListener("click", (e) => {
//     console.log(btn.parentNode);
//     console.log(e.target.parentNode.id);
//     console.log("ASDasda");
//     myModal.show();
//     console.log("ASDasdaasada");
//   });
// }

const deleConfirmBtn = document.getElementById("modal-delete-product-btn");
deleConfirmBtn.addEventListener("click", async (_) => {
  try {
    let id = sessionStorage.getItem("toDel");
    const url = `http://localhost:8080/admin/remove/?productid=${id}`;
    const response = await fetch(url, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
    });
    const jsData = await response.text();
    const d = JSON.parse(jsData);
    console.log("Completed!", d);
    if (d.status === "fail") {
      alert("Something Wrong");
    } else {
      // alert("Deleted Successfully!");
      showModal("Deleted Successfully!");
      let elem = document.getElementById(id);
      elem.remove();
      // getProductList();
    }
  } catch (err) {
    console.error(`Error: ${err}`);
  }
});
