package com.example.test.controller;

import com.example.test.model.*;
import com.example.test.repository.ProductRepository;
import com.example.test.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.test.repository.ShoppingCartRepository;
import com.example.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping(path = "/all")
    @CrossOrigin
    public @ResponseBody Iterable<User> getAllUser() {
        return userRepository.findAll();
    }

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


    // testing: show the current shopping cart for particular user with pagination enable
    @GetMapping(path = "/user/{id}/shoppingcart")
    @CrossOrigin
    public @ResponseBody Page<ShoppingCart> showShoppingCart(@PathVariable Integer id,
                                                                 @RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                                 @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize ) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        return shoppingCartRepository.findByShoppingCartId_User_Id(id, paging);
    }



    /**
     * testing: show all shopping cart with pagination enable
     * annotation: this function may not need to be used
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/user/shoppingcart")
    @CrossOrigin
    public @ResponseBody Page<ShoppingCart> showShoppingCart(@RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                                 @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        return shoppingCartRepository.findAll(paging);
    }

    // TODO: add product to the shopping list
    // assumption: the product and the user is already in the databse
    @GetMapping(path = "/user/{id}/shoppingcart/add")
    @CrossOrigin
    public @ResponseBody Map<String, Object> addShoppingCart(@PathVariable Integer userId,
                                                            @RequestParam(name="productId") String productId,
                                                            @RequestParam(name="size") Float size,
                                                            @RequestParam(name="quantity") Integer quantity) {

        Map<String, Object> resBody = new HashMap<>(3);
        Optional<User> addUserOptional = userRepository.findById(userId);
        if (addUserOptional.isEmpty()) {
            resBody.put("status", "fail");
            resBody.put("msg", "user does not exist");
            return resBody;
        }

        // TODO: add the rest of body after fixing the productrepository
        return resBody;
    }




    /**
     * testing: show product with pagnation enable
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/user/product")
    @CrossOrigin
    public @ResponseBody Page<Product> showProduct(@RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        return productRepository.findAll(paging);
    }

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
}
