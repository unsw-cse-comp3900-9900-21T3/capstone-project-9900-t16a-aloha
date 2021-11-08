package com.example.test.controller;

import com.example.test.model.*;
import com.example.test.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(path="/test")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private StorgeRepository storgeRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @GetMapping(path = "/all")
    @CrossOrigin
    public @ResponseBody Iterable<User> getAllUser() {
        return userRepository.findAll();
    }
    

    //=============================================================================
    // user register and login section
    //=============================================================================

    @PostMapping(path = "/register")
    @CrossOrigin
    public @ResponseBody
    Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> resBody = new HashMap<>(3);
    
        // check if required fields are empty
        if(user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "Empty parameters");
            return resBody;
        }
        // check if the account has been created
        User u1 = userRepository.findByEmail(user.getEmail());
        if(u1 != null) {
            resBody.put("status", "fail");
            resBody.put("msg", "User already exists");
            return resBody;
        }
        user.setTag(0);
        //user.setId(0);
        userRepository.save(user);
        //int id = userRepository.findByEmail(user.getEmail()).getId();
        int id = user.getId();
        resBody.put("status", "success");
        resBody.put("uid", id);
        resBody.put("email", user.getEmail());
        return resBody;
    }

    @PostMapping("/login")
    @CrossOrigin
    public @ResponseBody Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> resBody = new HashMap<>(3);
        User u = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (u == null) {
            resBody.put("status", "fail");
            resBody.put("msg", "Invalid email or password");
        }
        else {
            resBody.put("token", getJWTToken(u));
            resBody.put("status", "success");
            resBody.put("uid", u.getId());
            resBody.put("email", u.getEmail());
            // user role, 0 is customer and 1 is admin.
            resBody.put("role", u.getTag());
        }
        return resBody;
    }

    private String getJWTToken(User user) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(user.getId())).sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    //=============================================================================
    // get user address and personal information section
    //=============================================================================

    /**
     * get customer address information
     * @param id
     * @return
     */
    @GetMapping(path = "/user/{id}/address")
    @CrossOrigin
    public @ResponseBody Map<String, Object> getAddress(@PathVariable Integer id) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }
        User user = userToUpdateOption.get();
        Map<String, Object> resBody = new HashMap<>(3);
        resBody.put("userid", user.getId());
        resBody.put("street", user.getStreet());
        resBody.put("city", user.getCity());
        resBody.put("state", user.getState());
        resBody.put("postcode", user.getPostcode());
        resBody.put("firstname", user.getFirstName());
        resBody.put("lastname", user.getLastName());
        resBody.put("telephone", user.getTelephone());
        return resBody;
    }

    /**
     * get customer personal information
     * @param id
     * @return
     */
    @GetMapping(path = "/user/{id}/info")
    @CrossOrigin
    public @ResponseBody Map<String, Object> getUserInfo(@PathVariable Integer id) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }
        User user = userToUpdateOption.get();
        Map<String, Object> resBody = new HashMap<>(3);
        resBody.put("userid", user.getId());
        resBody.put("firstname", user.getFirstName());
        resBody.put("lastname", user.getLastName());
        resBody.put("telephone", user.getTelephone());
        resBody.put("email", user.getEmail());
        return resBody;
    }

    //=============================================================================
    // update user address and personal information section
    //=============================================================================

    /**
     * update customer account info such as firstname, lastname, email, telephone
     * @param id
     * @param user
     * @return
     */
    @PostMapping(path = "/user/{id}/account")
    @CrossOrigin
    public @ResponseBody User updateUserAccount(@PathVariable Integer id,  @RequestBody User user) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }
        // optional get id from user

        User userToUpdate = userToUpdateOption.get();

        if (user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getTelephone() != null) {
            userToUpdate.setTelephone(user.getTelephone());
        }

        User updatedUser = this.userRepository.save(userToUpdate);
        return updatedUser;
    }

    /**
     * update customer address info
     * @param id
     * @param user
     * @return
     */
    @PostMapping(path = "/user/{id}/address")
    @CrossOrigin
    public @ResponseBody User updateUserAddress(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }
        // optional get id from user
        User userToUpdate = userToUpdateOption.get();

        if (user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getTelephone() != null) {
            userToUpdate.setTelephone(user.getTelephone());
        }
        if (user.getCity() != null) {
            userToUpdate.setCity(user.getCity());
        }
        if (user.getPostcode() != null) {
            userToUpdate.setPostcode(user.getPostcode());
        }
        if (user.getState() != null) {
            userToUpdate.setState(user.getState());
        }
        if (user.getStreet() != null) {
            userToUpdate.setStreet(user.getStreet());
        }

        User updatedUser = this.userRepository.save(userToUpdate);
        return updatedUser;
    }

    //=============================================================================
    // product section
    //=============================================================================

    /**
     * show product with pagnation enable
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/user/product")
    @CrossOrigin
    public @ResponseBody Page<Product> showProduct(@RequestParam(name = "pageindex", defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(name = "pagesize", defaultValue = "8") Integer pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        return productRepository.findAllByVisibility(paging, 1);
    }

    /**
     * search product with optional brand, optional price range, pagination enable
     * sortby: name, price
     * order: asc, desc
     * @param brand
     * @param minPrice
     * @param maxPrice
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/user/search")
    @CrossOrigin
    public @ResponseBody Page<Product> filterProduct(@RequestParam(name = "productid", required = false) String id,
                                                     @RequestParam(name = "name", required = false) String name,
                                                     @RequestParam(name = "brand", required = false) String brand,
                                                     @RequestParam(name = "minprice", required = false) Float minPrice,
                                                     @RequestParam(name = "maxprice", required = false) Float maxPrice,
                                                     @RequestParam(name = "pageindex", required = false, defaultValue = "0") Integer pageIndex,
                                                     @RequestParam(name = "pagesize", required = false, defaultValue = "8") Integer pageSize,
                                                     @RequestParam(name = "sortby", required = false) String sortby,
                                                     @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        Pageable paging;
        if(sortby == null || (!sortby.equals("name") && !sortby.equals("price"))) {
            paging = PageRequest.of(pageIndex, pageSize);
        }
        else {
            if (order.equals("asc")) {
                paging = PageRequest.of(pageIndex, pageSize, Sort.by(sortby));
            }
            else {
                paging = PageRequest.of(pageIndex, pageSize, Sort.by(sortby).descending());
            }
        }
        // id
        if (id != null) {
            return productRepository.findByIdAndVisibilityAndIsDeleted(paging,id,1,0);
        }
        // name
        if (name != null && brand == null && minPrice == null && maxPrice == null) {
            return productRepository.findByNameContainsAndVisibilityAndIsDeleted(paging, name, 1, 0);
        }
        // brand
        if (name == null && brand != null && minPrice == null && maxPrice == null) {
            return productRepository.findByBrandAndVisibilityAndIsDeleted(paging, brand, 1, 0);
        }
        // < max price
        if (name == null && brand == null && minPrice == null && maxPrice !=null) {
            return productRepository.findByPriceIsLessThanAndVisibilityAndIsDeleted(paging, maxPrice,1,0);
        }
        // > min price
        if  (name == null && brand == null && minPrice != null && maxPrice ==null) {
            return productRepository.findByPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, minPrice, 1, 0);
        }
        // min price - max price
        if  (name == null && brand == null && minPrice != null && maxPrice !=null) {
            return productRepository.findByPriceBetweenAndVisibilityAndIsDeleted(paging,minPrice,maxPrice,1,0);
        }
        // name and brand
        if (name != null && brand != null && minPrice == null && maxPrice == null) {
            return productRepository.findByNameContainsAndBrandAndVisibilityAndIsDeleted(paging, name, brand, 1, 0);
        }
        // name and < max price
        if (name != null && brand == null && minPrice == null && maxPrice !=null) {
            return productRepository.findByNameContainsAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, name, maxPrice, 1, 0);
        }
        // name and > min price
        if (name != null && brand == null && minPrice != null && maxPrice ==null) {
            return productRepository.findByNameContainsAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, name, minPrice, 1, 0);
        }
        // name and min price - max price
        if (name != null && brand == null && minPrice != null && maxPrice !=null) {
            return productRepository.findByNameContainsAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, name, minPrice, maxPrice,1 , 0);
        }
        // brand and < max price
        if (name == null && brand != null && minPrice == null && maxPrice !=null) {
            return productRepository.findByBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, brand, maxPrice, 1, 0);
        }
        // brand and > min price
        if (name == null && brand != null && minPrice != null && maxPrice ==null) {
            return productRepository.findByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, brand, minPrice, 1, 0);
        }
        // brand and min price - max price
        if (name == null && brand != null && minPrice != null && maxPrice !=null) {
            return productRepository.findByBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, brand, minPrice,maxPrice, 1, 0);
        }
        // name and brand and < max price
        if (name != null && brand != null && minPrice == null && maxPrice != null) {
            return productRepository.findByNameContainsAndBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, name, brand, maxPrice,1, 0);
        }
        // name and brand and > min price
        if (name != null && brand != null && minPrice != null && maxPrice == null) {
            return productRepository.findByNameContainsAndBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, name, brand, maxPrice,1, 0);
        }
        // name and brand and min price - max price
        if (name != null && brand != null && minPrice != null && maxPrice != null) {
            return productRepository.findByNameContainsAndBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, name, brand, minPrice, maxPrice,1, 0);
        }
        return productRepository.findAllByVisibility(paging, 1);
    }

    //=============================================================================
    // shoppingcart section
    //=============================================================================

    /**
     * show shopping cart for particular user (not pargination enable)
     * @param userId
     * @return
     */
    @GetMapping(path = "/user/shoppingcart/show")
    @CrossOrigin
    public @ResponseBody ArrayList<Object> showShoppingCart(@RequestParam(name = "userid") Integer userId) {
        ArrayList<Object> res = new ArrayList<>();
        Iterable<ShoppingCart> shoppingCarts =  shoppingCartRepository.findByShoppingCartId_User_Id(userId);
        for (ShoppingCart s : shoppingCarts) {
            Map<String,Object> shoppingCart = new LinkedHashMap<>(3);
            Product product = s.getShoppingCartId().getProduct();
            shoppingCart.put("productID", product.getId());
            shoppingCart.put("size", s.getShoppingCartId().getSize());
            shoppingCart.put("quantity", s.getQuantity());
            res.add(shoppingCart);
        }
        return res;
    }

    /**
     * add product to shopping cart
     * @param userId
     * @param productId
     * @param size
     * @param quantity
     * @return
     */
    @PostMapping(path = "/user/shoppingcart/add")
    @CrossOrigin
    public @ResponseBody Map<String, Object> addShoppingCart(@RequestParam(name = "userid") Integer userId,
                                                            @RequestParam(name="productid") String productId,
                                                            @RequestParam(name="size") Float size,
                                                            @RequestParam(name="quantity") Integer quantity) {

        Map<String, Object> resBody = new HashMap<>(3);
        Optional<User> addUserOptional = userRepository.findById(userId);
        if (addUserOptional.isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "user does not exist");
            return resBody;
        }

        Optional<Product> addProductOptional = productRepository.findById(productId);
        if (addProductOptional.isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "product does not exist");
            return resBody;
        }

        Product p = addProductOptional.get();
        User u = addUserOptional.get();

        ShoppingCartId shoppingCartId = new ShoppingCartId();
        shoppingCartId.setProduct(p);
        shoppingCartId.setSize(size);
        shoppingCartId.setUser(u);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        ShoppingCart shoppingCart;
        if (optionalShoppingCart.isPresent()) {
            shoppingCart = optionalShoppingCart.get();
            int quantity1 = shoppingCart.getQuantity();
            shoppingCart.setQuantity(quantity1+quantity);

        }
        else {
            shoppingCart = new ShoppingCart();
            shoppingCart.setShoppingCartId(shoppingCartId);
            shoppingCart.setQuantity(quantity);
        }
        shoppingCartRepository.save(shoppingCart);


        resBody.put("status", "succeed");
        resBody.put("msg", "added to the shoppingcart");
        return resBody;
    }

    /**
     * Remove a product from shoppingcart
     * @param userId
     * @param productId
     * @param size
     */
    @PostMapping(path = "/user/shoppingcart/remove")
    @CrossOrigin
    public @ResponseBody Map<String, Object> removeFromShoppingcart(@RequestParam(name = "userid") Integer userId,
                                                                    @RequestParam(name = "productid") String productId,
                                                                    @RequestParam(name = "size") Float size,
                                                                    @RequestParam(name="quantity") Integer quantity) {
        Map<String, Object> resBody = new HashMap<>(3);
        Optional<User> addUserOptional = userRepository.findById(userId);
        if (addUserOptional.isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "user does not exist");
            return resBody;
        }

        Optional<Product> addProductOptional = productRepository.findById(productId);
        if (addProductOptional.isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "product does not exist");
            return resBody;
        }

        Product p = addProductOptional.get();
        User u = addUserOptional.get();

        ShoppingCartId shoppingCartId = new ShoppingCartId();
        shoppingCartId.setProduct(p);
        shoppingCartId.setSize(size);
        shoppingCartId.setUser(u);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(shoppingCartId);
        shoppingCart.setQuantity(quantity);

        shoppingCartRepository.delete(shoppingCart);
        resBody.put("status", "succeed");
        resBody.put("msg", "delete product from shoppingcart");
        return resBody;
    }

    //=============================================================================
    // wishlist section
    //=============================================================================

    /**
     * Add a product to wishlist
     * @param userId
     * @param productId
     * @param size
     */
    @PostMapping(path = "/user/wishlist/add")
    @CrossOrigin
    public @ResponseBody Map<String, Object> addToWishlist(@RequestParam(name =  "userid") Integer userId, @RequestParam(name = "productid") String productId,
                                                           @RequestParam(name = "size") Float size) {
        Map<String, Object> resBody = new HashMap<>(3);
        Date date = new Date();
        Optional<Product> product = productRepository.findById(productId);
        Optional<User> user = userRepository.findById(userId);
        WishlistId wishlistId =new WishlistId();
        wishlistId.setProduct(product.get());
        wishlistId.setUser(user.get());
        wishlistId.setSize(size);
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        wishlist.setAddTime(date);
        wishlistRepository.save(wishlist);
        resBody.put("msg", "succeed");
        return resBody;
    }

    /**
     * Remove a product from list
     * @param userId
     * @param productId
     * @param size
     */
    @PostMapping(path = "/user/wishlist/remove")
    @CrossOrigin
    public @ResponseBody Map<String, Object> removeFromWishlist(@RequestParam(name = "userid") Integer userId,
                                                                @RequestParam(name = "productid") String productId,
                                                                @RequestParam(name = "size") Float size) {
        Map<String, Object> resBody = new HashMap<>(3);
        Optional<Product> product = productRepository.findById(productId);
        Optional<User> user = userRepository.findById(userId);
        WishlistId wishlistId =new WishlistId();
        wishlistId.setProduct(product.get());
        wishlistId.setUser(user.get());
        wishlistId.setSize(size);
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        wishlistRepository.delete(wishlist);
        resBody.put("msg", "succeed");
        return resBody;
    }

    /**
     * Show an user's wishlist
     * @param userid
     */
    @GetMapping(path = "/user/wishlist/show")
    @CrossOrigin
    public @ResponseBody
    ArrayList<Object> showWishlist(@RequestParam Integer userid) {
        ArrayList<Object> res = new ArrayList<>();
        Iterable<Wishlist> wishlists =  wishlistRepository.findByWishlistId_User_Id(userid);
        for(Wishlist w:wishlists) {
            Map<String,Object>  wishlist = new LinkedHashMap<>(3);
            Product product = w.getWishlistId().getProduct();
            wishlist.put("productID",product.getId());
            wishlist.put("name",product.getName());
            wishlist.put("size",w.getWishlistId().getSize());
            wishlist.put("price", product.getPrice());
            res.add(wishlist);
        }
        return res;
    }

    @PostMapping(path = "/creatOrder/{id}")
    @CrossOrigin
    public @ResponseBody Map<String, Object> createOrder(@PathVariable(name = "id") Integer userid, @RequestBody OrderHistory order) {
        Map<String, Object> res = new LinkedHashMap<>();
        Date date = new Date();
        ArrayList<ShoppingCart> items = new ArrayList<>();
        OrderHistory orderHistory = new OrderHistory();
        // insert data into order history table
        try {

            Iterable<ShoppingCart> shoppingCarts =  shoppingCartRepository.findByShoppingCartId_User_Id(userid);
            float cost = 0;
            // calculate total cost
            for(ShoppingCart s: shoppingCarts) {
                items.add(s);
                ShoppingCartId shoppingCartId = s.getShoppingCartId();
                Product product = shoppingCartId.getProduct();
                int quantity = s.getQuantity();
                cost += quantity * product.getPrice();
            }
            // while shopping cart is empty
            if (items.size() == 0) {
                res.put("status", "fail");
                res.put("msg", "Shopping cart is empty");
                return res;
            }
            // save result to order history table
            order.setTotalCost(cost);
            order.setUserId(userid);
            order.setOrderTime(date);
            orderHistory = orderHistoryRepository.save(order);
            orderHistoryRepository.flush();



        }
        catch (Exception e) {
            res.put("status", "fail");
            res.put("msg", e.getMessage());
            return res;
        }
        //insert data into order detail table;
        try {

            for(ShoppingCart s: items) {
                // remove from shopping cart
                ShoppingCartId shoppingCartId = s.getShoppingCartId();
                shoppingCartRepository.deleteById(shoppingCartId);
                // reduce the stock
                StorgeId storgeId = new StorgeId();
                storgeId.setProduct(shoppingCartId.getProduct());
                storgeId.setSize(shoppingCartId.getSize());
                Optional<Storge> optional = storgeRepository.findById(storgeId);
                Storge storge = optional.get();
                int stock = storge.getStock();
                storge.setStock(stock - s.getQuantity());
                storgeRepository.save(storge);
                // insert into order detail
                OrderId orderId = new OrderId();
                orderId.setOrderHistory(orderHistory);
                orderId.setProduct(shoppingCartId.getProduct());
                orderId.setSize(shoppingCartId.getSize());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setQuantity(s.getQuantity());
                orderDetailRepository.save(orderDetail);
            }


        }
        catch (Exception e) {
            res.put("status", "fail");
            res.put("msg", e.getMessage());
            return res;
        }
        res.put("status", "success");
        res.put("orderid", orderHistory.getId());
        return res;
    }

    @GetMapping(path = "/getOrders/{id}")
    @CrossOrigin
    public @ResponseBody ArrayList<Map<String, Object>> getOrders(@PathVariable(name = "id") Integer userid) {
        Iterable<OrderHistory> orderHistories = orderHistoryRepository.findByUserIdOrderByOrderTimeDesc(userid);
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        for(OrderHistory order: orderHistories) {
            Iterable<OrderDetail> orderDetails = orderDetailRepository.findByOrderId_OrderHistory_Id(order.getId());
            Map<String, Object> orderInfo = new LinkedHashMap<>();
            orderInfo.put("orderid", order.getId());
            orderInfo.put("phone", order.getTelephone());
            orderInfo.put("firstName", order.getFirstname());
            orderInfo.put("lastName", order.getLastname());
            orderInfo.put("street", order.getStreet());
            orderInfo.put("city", order.getCity());
            orderInfo.put("postcode", order.getPostcode());
            orderInfo.put("state", order.getState());
            orderInfo.put("orderTime", order.getOrderTime());
            ArrayList<Map<String, Object>> products = new ArrayList<>();
            for(OrderDetail orderDetail: orderDetails) {
                Map<String, Object> product = new LinkedHashMap<>(3);
                product.put("id", orderDetail.getOrderId().getProduct().getId());
                product.put("size", orderDetail.getOrderId().getSize());
                product.put("qty", orderDetail.getQuantity());
                products.add(product);
            }
            orderInfo.put("products", products);
            res.add(orderInfo);
        }
        return res;
    }

    @GetMapping(path = "/getRecommend/{id}")
    @CrossOrigin
    public @ResponseBody Recommend getRecommend(@PathVariable(name = "id") Integer userid) {
        User user = userRepository.findById(userid).get();
        Product p = productRepository.findRandom();
        Iterable<Wishlist> wishlists = wishlistRepository.findByWishlistId_User_Id(userid);
        String pid = user.getPreferred();
        int size = 0;
        for(Wishlist w: wishlists) {
            size++;
        }
        if(size !=0) {
            for(Wishlist w:wishlists) {
                user.setPreferred(w.getWishlistId().getProduct().getId());
                userRepository.save(user);
                pid = user.getPreferred();
                break;
            }
        }
        else if(user.getPreferred() == null) {
            if (user.getLastVisited() == null) {
                pid = p.getId();
                user.setPreferred(pid);
            }
            else {
                user.setPreferred(user.getLastVisited());
                pid = user.getLastVisited();
            }
            userRepository.save(user);
        }

        Recommend r = recommendRepository.findById(pid).get();
        return r;
    }
}
