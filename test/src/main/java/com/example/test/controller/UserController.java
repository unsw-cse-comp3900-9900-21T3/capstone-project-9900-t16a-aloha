package com.example.test.controller;

import com.example.test.model.*;
import com.example.test.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;


import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.DatatypeConverter;


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

    @Autowired
    private ReviewRepository reviewRepository;

    @Value("${mail.fromMail.sender}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, Object> codeMap = new HashMap<>();

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
                                                     @RequestParam(name = "order", required = false, defaultValue = "desc") String order,
                                                     @RequestParam(name = "instock", required = false, defaultValue = "false") String instock) {
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
            if(instock.equals("true")) return productRepository.findByIdAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging,id,1,0,0);
            return productRepository.findByIdAndVisibilityAndIsDeleted(paging,id,1,0);
        }
        // name
        if (name != null && brand == null && minPrice == null && maxPrice == null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, 1, 0,0);
            return productRepository.findByNameContainsAndVisibilityAndIsDeleted(paging, name, 1, 0);
        }
        // brand
        if (name == null && brand != null && minPrice == null && maxPrice == null) {
            if(instock.equals("true")) return productRepository.findDistinctByBrandAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, brand, 1, 0,0);
            return productRepository.findByBrandAndVisibilityAndIsDeleted(paging, brand, 1, 0);
        }
        // < max price
        if (name == null && brand == null && minPrice == null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging,maxPrice,1,0,0);
            return productRepository.findByPriceIsLessThanAndVisibilityAndIsDeleted(paging, maxPrice,1,0);
        }
        // > min price
        if  (name == null && brand == null && minPrice != null && maxPrice ==null) {
            if(instock.equals("true")) return productRepository.findDistinctByPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, minPrice,1,0,0);
            return productRepository.findByPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, minPrice, 1, 0);
        }
        // min price - max price
        if  (name == null && brand == null && minPrice != null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByPriceBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, minPrice, maxPrice, 1, 0, 0);
            return productRepository.findByPriceBetweenAndVisibilityAndIsDeleted(paging,minPrice,maxPrice,1,0);
        }
        // name and brand
        if (name != null && brand != null && minPrice == null && maxPrice == null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndBrandAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, brand, 1, 0,0);
            return productRepository.findByNameContainsAndBrandAndVisibilityAndIsDeleted(paging, name, brand, 1, 0);
        }
        // name and < max price
        if (name != null && brand == null && minPrice == null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, maxPrice, 1, 0,0);
            return productRepository.findByNameContainsAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, name, maxPrice, 1, 0);
        }
        // name and > min price
        if (name != null && brand == null && minPrice != null && maxPrice ==null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, minPrice, 1, 0,0);
            return productRepository.findByNameContainsAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, name, minPrice, 1, 0);
        }
        // name and min price - max price
        if (name != null && brand == null && minPrice != null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, minPrice, maxPrice,1 , 0, 0);
            return productRepository.findByNameContainsAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, name, minPrice, maxPrice,1 , 0);
        }
        // brand and < max price
        if (name == null && brand != null && minPrice == null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByBrandAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, brand, maxPrice, 1, 0, 0);
            return productRepository.findByBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, brand, maxPrice, 1, 0);
        }
        // brand and > min price
        if (name == null && brand != null && minPrice != null && maxPrice ==null) {
            if(instock.equals("true")) return productRepository.findDistinctByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, brand, minPrice, 1, 0,0);
            return productRepository.findByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, brand, minPrice, 1, 0);
        }
        // brand and min price - max price
        if (name == null && brand != null && minPrice != null && maxPrice !=null) {
            if(instock.equals("true")) return productRepository.findDistinctByBrandAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, brand, minPrice,maxPrice, 1, 0,0);
            return productRepository.findByBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, brand, minPrice,maxPrice, 1, 0);
        }
        // name and brand and < max price
        if (name != null && brand != null && minPrice == null && maxPrice != null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndBrandAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, brand, maxPrice,1, 0,0);
            return productRepository.findByNameContainsAndBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(paging, name, brand, maxPrice,1, 0);
        }
        // name and brand and > min price
        if (name != null && brand != null && minPrice != null && maxPrice == null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndBrandAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, brand, maxPrice,1, 0,0);
            return productRepository.findByNameContainsAndBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(paging, name, brand, maxPrice,1, 0);
        }
        // name and brand and min price - max price
        if (name != null && brand != null && minPrice != null && maxPrice != null) {
            if(instock.equals("true")) return productRepository.findDistinctByNameContainsAndBrandAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging, name, brand, minPrice, maxPrice,1, 0,0);
            return productRepository.findByNameContainsAndBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(paging, name, brand, minPrice, maxPrice,1, 0);
        }
        if(instock.equals("true")) return productRepository.findDistinctAllByVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging,1 , 0,0);
        return productRepository.findAllByVisibilityAndIsDeleted(paging, 1, 0);
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
        // check storage
        StorgeId storgeId = new StorgeId();
        storgeId.setSize(size);
        storgeId.setProduct(p);
        Optional<Storge> optionalStorge = storgeRepository.findById(storgeId);
        if(!optionalStorge.isPresent()) {
            resBody.put("status", "fail");
            resBody.put("msg", "product stock information is unavailable");
            return resBody;
        }
        Storge storge = optionalStorge.get();
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        ShoppingCart shoppingCart;
        if (optionalShoppingCart.isPresent()) {
            shoppingCart = optionalShoppingCart.get();
            int quantity1 = shoppingCart.getQuantity();
            if(quantity1 + quantity > storge.getStock()) {
                resBody.put("status", "fail");
                resBody.put("msg", "out of stock");
                return resBody;
            }
            shoppingCart.setQuantity(quantity1+quantity);

        }
        else {
            if(quantity > storge.getStock()) {
                resBody.put("status", "fail");
                resBody.put("msg", "out of stock");
                return resBody;
            }
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
        List<String> finalPickArray = new ArrayList<>();
        int numWishToChoose = 5;
        List<String> potentialProducts = new ArrayList<>();

        Set<String> items = new HashSet<>(8);
        Iterable<OrderHistory> orderHistories = orderHistoryRepository.findByUserIdOrderByOrderTimeDesc(userid);
        ArrayList<String> orderItems = new ArrayList<>(8);

        // pick random item from history
        for (OrderHistory o : orderHistories) {
            Iterable<OrderDetail> orderDetails = orderDetailRepository.findByOrderId_OrderHistory_Id(o.getId());
            for (OrderDetail od : orderDetails) {
                String pid = od.getOrderId().getProduct().getId();
                if (!items.contains(pid)) {
                    items.add(pid);
                    orderItems.add(pid);
                }
            }
        }
        System.out.println("hlo");
        items.clear();

        if (!orderItems.isEmpty()) {
            Random random = new Random();
            int n = random.nextInt(orderItems.size());
            finalPickArray.add(orderItems.get(n));
        }

        // add last review
        User user = userRepository.findById(userid).get();
        if (user.getLastVisited() != null) {
            finalPickArray.add(user.getLastVisited());
        }
        System.out.println("hll");

        // add random one from recommend
        Recommend r = recommendRepository.findRandom();
        finalPickArray.add(r.getId());
        r = recommendRepository.findRandom();
        potentialProducts.add(r.getId());

        Iterable<Wishlist> wishlists = wishlistRepository.findByWishlistId_User_Id(userid);
        ArrayList<String> wishItems = new ArrayList<>(8);
        for (Wishlist w : wishlists) {
            String pid = w.getWishlistId().getProduct().getId();
            if (!items.contains(pid)) {
                wishItems.add(pid);
            }
        }
        System.out.println("yolos");
        int i = 0;
        while (i < wishItems.size() && i < numWishToChoose) {
            Random random = new Random();
            int n = random.nextInt(wishItems.size());

            String productId = wishItems.get(n);
            finalPickArray.add(productId);

            Optional<Recommend> optionalRecommends = recommendRepository.findById(productId);
            if (optionalRecommends.isEmpty()) {
                int loop = 4;
                while (loop > 0) {
                    Recommend recom = recommendRepository.findRandom();
                    potentialProducts.add(recom.getId());
                    --loop;
                }
            } else {
                Recommend recommend = optionalRecommends.get();
                potentialProducts.add(recommend.getS1());
                potentialProducts.add(recommend.getS2());
                potentialProducts.add(recommend.getS3());
                potentialProducts.add(recommend.getS4());
            }
            ++i;
        }
        System.out.println("yoyo");

        int count = finalPickArray.size();
        while (count < 10) {
            Random random = new Random();
            int n = random.nextInt(potentialProducts.size());
            finalPickArray.add(potentialProducts.get(n));
            ++count;
        }

        System.out.println("samnn");

        if (user.getPreferred() != null) {
            finalPickArray.add(user.getPreferred());
        }
        Random random = new Random();
        int n = random.nextInt(finalPickArray.size());
        System.out.println("yamm");

        // user.setPreferred(finalPickArray.get(n));
        // userRepository.save(user);

        return recommendRepository.findById(finalPickArray.get(n)).get();
    }

    //=============================================================================
    // recommend section
    //=============================================================================

    /*
    @GetMapping(path = "/getRecommend/{id}")
    @CrossOrigin
    public @ResponseBody Recommend getRecommend(@PathVariable(name = "id") Integer userid) {
        User user = userRepository.findById(userid).get();
        Product p = productRepository.findRandom();
        String userFav = user.getPreferred();
        String lastView = user.getLastVisited();
        Set<String> items = new HashSet<>(8);
        Iterable<OrderHistory> orderHistories = orderHistoryRepository.findByUserIdOrderByOrderTimeDesc(userid);
        ArrayList<String> orderItems = new ArrayList<>(8);
        for (OrderHistory o : orderHistories) {
            Iterable<OrderDetail> orderDetails = orderDetailRepository.findByOrderId_OrderHistory_Id(o.getId());
            for (OrderDetail od : orderDetails) {
                String pid = od.getOrderId().getProduct().getId();
                if (!items.contains(pid)) {
                    items.add(pid);
                    orderItems.add(pid);
                }
            }
        }
        items.clear();
        Iterable<Wishlist> wishlists = wishlistRepository.findByWishlistId_User_Id(userid);
        ArrayList<String> wishItems = new ArrayList<>(8);
        for (Wishlist w : wishlists) {
            String pid = w.getWishlistId().getProduct().getId();
            if (!items.contains(pid)) {
                wishItems.add(pid);
            }
        }
        items.clear();
        if (wishItems.size() != 0) {
            Random random = new Random();
            int n = random.nextInt(wishItems.size());
            if (userFav == null) {
                userFav = wishItems.get(n);
                user.setPreferred(userFav);
                userRepository.save(user);
<<<<<<< HEAD
            } else {
                if (!wishItems.contains(userFav)) {
                    userFav = wishItems.get(n);
                    user.setPreferred(userFav);
                    userRepository.save(user);
                }
=======
            }
            else {
                userFav = wishItems.get(n);
                user.setPreferred(userFav);
                userRepository.save(user);

>>>>>>> 67f09b455b34a041f31c4b1e1db0e3d8388aa2e1
            }
        } else {
            ArrayList<String> union = new ArrayList<>(8);
            if (lastView != null) {
                union.add(lastView);
                items.add(lastView);
            }
            for (String s : orderItems) {
                if (!items.contains(s)) {
                    union.add(s);
                    items.add(s);
                }
            }
            if (union.size() == 0) {
                userFav = p.getId();
            } else {
                Random random = new Random();
                int n = random.nextInt(union.size());
                userFav = union.get(n);
            }
            user.setPreferred(userFav);
            userRepository.save(user);
        }
<<<<<<< HEAD
        if (lastView != null && !lastView.equals(userFav)) {
=======
        if(lastView != null && !lastView.equals(userFav) && !lastView.startsWith("_")) {
>>>>>>> 67f09b455b34a041f31c4b1e1db0e3d8388aa2e1
            items.clear();
//            Recommend recommend1 = recommendRepository.findById(lastView).get();
//            Recommend recommend2 = recommendRepository.findById(userFav).get();
            Random random = new Random();
            int n = random.nextInt(2);
            String finalRecommend = n == 1? lastView: userFav;
            Recommend recommend1 = recommendRepository.findById(finalRecommend).get();
            Recommend res = new Recommend();
            ArrayList<String> recommends = new ArrayList<>();
            items.add(recommend1.getS1());
            items.add(recommend1.getS2());
            items.add(recommend1.getS3());
            items.add(recommend1.getS4());
            items.add(recommend1.getS5());
            items.add(recommend1.getS6());
            items.add(recommend1.getS7());
            items.add(recommend1.getS8());
            recommends.addAll(items);
<<<<<<< HEAD
            while (items.size() < 8) {
                Random random = new Random();
                int n = random.nextInt(items.size());
                Recommend r = recommendRepository.findById(recommends.get(n)).get();
                if (items.add(r.getS1())) {
                    recommends.add(r.getS1());
                }
=======
            while(recommends.size() < 8) {
                n = random.nextInt(items.size());
                Recommend r = recommendRepository.findById(recommends.get(n)).get();
                //if(items.add(r.getS1())) {
                recommends.add(r.getS1());
                //}
>>>>>>> 67f09b455b34a041f31c4b1e1db0e3d8388aa2e1

            }
            res.setId(finalRecommend);
            res.setS1(recommends.get(0));
            res.setS2(recommends.get(1));
            res.setS3(recommends.get(2));
            res.setS4(recommends.get(3));
            res.setS5(recommends.get(4));
            res.setS6(recommends.get(5));
            res.setS7(recommends.get(6));
            res.setS8(recommends.get(7));
            return res;
<<<<<<< HEAD
        } else {
            Recommend res = recommendRepository.findById(userFav).get();
=======
        }
        else {
            Recommend res;
            if(userFav.startsWith("_")) {
                res = recommendRepository.findRandom();
            }
            else {
                res = recommendRepository.findById(userFav).get();
            }
>>>>>>> 67f09b455b34a041f31c4b1e1db0e3d8388aa2e1
            ArrayList<String> recommends = new ArrayList<>();
            items.add(res.getS1());
            items.add(res.getS2());
            items.add(res.getS3());
            items.add(res.getS4());
            items.add(res.getS5());
            items.add(res.getS6());
            items.add(res.getS7());
            items.add(res.getS8());
            recommends.addAll(items);
<<<<<<< HEAD
            while (items.size() < 8) {
                Random random = new Random();
                int n = random.nextInt(items.size());
                Recommend r = recommendRepository.findById(recommends.get(n)).get();
                if (items.add(r.getS1())) {
                    recommends.add(r.getS1());
                }
=======
            while(recommends.size() < 8) {
                Random random = new Random();
                int n = random.nextInt(items.size());
                Recommend r = recommendRepository.findById(recommends.get(n)).get();
                //if(items.add(r.getS1())) {
                recommends.add(r.getS1());
               // }
>>>>>>> 67f09b455b34a041f31c4b1e1db0e3d8388aa2e1

            }
            res.setS1(recommends.get(0));
            res.setS2(recommends.get(1));
            res.setS3(recommends.get(2));
            res.setS4(recommends.get(3));
            res.setS5(recommends.get(4));
            res.setS6(recommends.get(5));
            res.setS7(recommends.get(6));
            res.setS8(recommends.get(7));
            return res;
        }


    }

     */



    // Annotation: empty
    @GetMapping(path = "/user/getReview/")
    @CrossOrigin
    public @ResponseBody ArrayList<Map<String, Object>> getReview(@RequestParam(name = "orderid") Integer orderId) {
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        // check if the order was placed before leave the review
        Optional<OrderHistory> optionalOrderHistory = orderHistoryRepository.findById(orderId);
        if (optionalOrderHistory.isEmpty()) {
            Map<String, Object> singleResponse= new LinkedHashMap<>();
            singleResponse.put("status", "fail");
            singleResponse.put("msg", "order not placed yet");
            res.add(singleResponse);
            return res;
        }

        OrderHistory orderHistory = optionalOrderHistory.get();

        Iterable<OrderDetail> orderDetails = orderDetailRepository.findByOrderId_OrderHistory_Id(orderId);
        for (OrderDetail orderDetail : orderDetails) {
            Map<String, Object> singleResponse = new LinkedHashMap<>();

            Product product = orderDetail.getOrderId().getProduct();
            Review review = reviewRepository.findByOrderHistoryAndProduct(orderHistory, product);
            if (review == null) {
                singleResponse.put("productid", product.getId());
                singleResponse.put("size", orderDetail.getOrderId().getSize());
                singleResponse.put("rating", -1);
            } else {
                singleResponse.put("productid", product.getId());
                singleResponse.put("size", review.getSize());
                singleResponse.put("rating", review.getRating());
            }
            res.add(singleResponse);
         }

         return res;
    }

    @PostMapping(path = "/user/postReview/")
    @CrossOrigin
    public @ResponseBody Map<String, Object> postReview(@RequestParam(name = "productid") String productId,
                                                        @RequestParam(name = "orderid") Integer orderId,
                                                        @RequestParam(name = "size") Float size,
                                                        @RequestParam(name = "rating") Float rate) {
        Map<String, Object> res = new LinkedHashMap<>();
        // check if enought information is given
        if (productId == null || orderId == null || size == null || rate == null) {
            res.put("status", "fail");
            res.put("msg", "information given missing, need to provide orderId, productId, and size");
            return res;
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            res.put("status", "fail");
            res.put("msg", "cannot find the product");
            return res;
        }

        Optional<OrderHistory> optionalOrder = orderHistoryRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            res.put("status", "fail");
            res.put("msg", "cannot find the order");
            return res;
        }

        Product product = optionalProduct.get();
        OrderHistory orderHistory = optionalOrder.get();

        Review duplicateReview = reviewRepository.findByOrderHistoryAndProduct(orderHistory, product);
        if (duplicateReview != null) {
            res.put("status", "fail");
            res.put("msg", "order with this product is already reviewed");
            return res;
        }

        Review review = new Review();
        review.setOrderHistory(orderHistory);
        review.setProduct(product);
        review.setSize(size);
        review.setRating(rate);
        reviewRepository.save(review);

        res.put("status", "success");
        res.put("msg", "review is updated");

        return res;
    }
    @GetMapping(path = "/instock")
    @CrossOrigin
    public @ResponseBody Page<Product> instock(@RequestParam(name = "maxprice") Float maxPrice,@RequestParam Integer stock) {
        PageRequest paging = PageRequest.of(0 , 8);
        //return productRepository.findByPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stock(paging,maxPrice,1,0,122);
        return productRepository.findDistinctByPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(paging,maxPrice,1,0,0);

    }

    private String genCode(int n) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < n; ++i) {
            int rand = random.nextInt(10);
            stringBuffer.append(rand);
        }
        return stringBuffer.toString();
    }

    @GetMapping(path = "/sendemail")
    @CrossOrigin
    public @ResponseBody String sendEmail(@RequestParam(name = "email") String email) {
        User user = userRepository.findByEmail(email);
        if( user == null) {
            return "User does not exist";
        }
        int userid = user.getId();
        if(codeMap.containsKey("tm"+userid)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            Date startTime;
            try {
                startTime = simpleDateFormat.parse(codeMap.get("tm"+userid).toString());
            } catch (ParseException e) {
                return e.getMessage();
            }
            int minutes =  (int) ((currentTime.getTime()- startTime.getTime())/(1000 * 60));
            if(minutes <=1) {
                return "Please re-send the email 1 minute later";
            }
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String code = genCode(6);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Reset your password");
        simpleMailMessage.setText("Hello, we have received your request of resetting password, your verify code is [" + code + "]. The code will be invalid in 10 minutes.");
        try {
            javaMailSender.send(simpleMailMessage);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String currentTime = simpleDateFormat.format(calendar.getTime());
            // md5 encryption
//            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.update(code.getBytes());
//            byte[] digest = messageDigest.digest();
//            String hashCode = DatatypeConverter.printHexBinary(digest).toUpperCase();
            String hash = "hash"+userid;
            String timeStamp = "tm"+userid;
            codeMap.put(hash, code);
            codeMap.put(timeStamp, currentTime);
            return "success";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "/resetpwd")
    @CrossOrigin
    public @ResponseBody String resetPassword(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code, @RequestParam(name = "password") String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return "User does not exist";
        }
        int userid = user.getId();
        String hash = "hash"+ userid;
        String timeStamp = "tm"+ userid;
        if(codeMap.size() == 0 || !codeMap.containsKey(hash)) {
            return "Please verify your email first";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        Date startTime;
        try {
            startTime = simpleDateFormat.parse(codeMap.get(timeStamp).toString());
        } catch (ParseException e) {
            return e.getMessage();
        }
        int minutes =  (int) ((currentTime.getTime()- startTime.getTime())/(1000 * 60));
        if (minutes <= 10 && minutes >= 0) {
            String lastCode = codeMap.get(hash).toString();
            if(lastCode.equals(code)) {
                user.setPassword(password);
                userRepository.save(user);
                codeMap.put(hash, "set");
                return "success";
            }
            else {
                return "Wrong code";
            }
        }
        else {
            return "The code has expired";
        }


    }



}
