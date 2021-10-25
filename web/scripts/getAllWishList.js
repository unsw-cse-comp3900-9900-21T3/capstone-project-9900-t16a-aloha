const getAllWishList = async () => {
  const userId = sessionStorage.getItem("userID");
  const url = `http://localhost:8080/test/user/wishlist/show?userid=${userId}`;
  const response = await fetch(url, {
    method: "get",
    headers: {
      "content-type": "application/json",
    },
  });
  const jsData = await response.text();
  const d = JSON.parse(jsData);

  if (d.status === "fail") {
    alert("Something Wrong");
  } else {
    return d;
  }
};

export { getAllWishList };
