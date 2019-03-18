package org.egc.sao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.util.AuthUtil;
import org.egc.sao.util.DateUtil;
import org.egc.sao.domain.User;
import org.egc.sao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "http://localhost:7099", maxAge = 3600)
public class UserController {

    @Autowired
    UserService us;

    @RequestMapping(value = "account",method = RequestMethod.POST)
    public Result saveUser(
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String email
    ){
        if(us.findUser(new User().setEmail(email))==null){
            UUID id=UUID.randomUUID();
            User user=new User()
                    .setId(id)
                    .setName(name)
                    .setPassword(password)
                    .setEmail(email)
                    .setCreatedOn(LocalDateTime.now());
            us.insert(user);
        }else{
            return new Result<>(ResInfo.CONFILICT,email);
        }
        return new Result<>(ResInfo.SUCCESS,null);
    }

    @RequestMapping(value = "account",method = RequestMethod.GET)
    public Result listUser(
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ){
        if(token!=null&&!AuthUtil.isJwtValide(token)){
            return new Result<>(ResInfo.AUTH_FAIL,null);
        }

        User user=new User()
                .setId(token!=null?UUID.fromString(token):null)
                .setName(name)
                .setEmail(email);
        User target = us.findUser(user);
        if(target==null){
            return new Result<>(ResInfo.NOT_FOUND,null);
        }
        target.getRoles().add(us.findRole(target));
        return new Result<>(ResInfo.SUCCESS,target);
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public Result login(
            @RequestBody String jsonString
    ){
        JSONObject jo= JSON.parseObject(jsonString);
        String name=jo.getString("name");
        String email=jo.getString("email");
        String password=jo.getString("password");

        if(name==null&&email==null||password==null){
            return new Result<>(ResInfo.BAD_REQUEST,jsonString);
        }
        User target = us.findUser(new User().setName(name).setEmail(email));
        if(target==null){
            return new Result<>(ResInfo.NOT_FOUND,null);
        }
        if(!password.equals(target.getPassword())){
            return new Result<>(ResInfo.AUTH_FAIL,null);
        }
        JSONObject result=new JSONObject();

        result.put("token",AuthUtil.createJwt(target.getId().toString()));
        return new Result<>(ResInfo.SUCCESS, result);

//        if(user==null||user.getPassword()==null||user.getName()==null&&user.getEmail()==null){
//            return new Result<>(ResInfo.BAD_REQUEST,user);
//        }
//        User target = us.findUser(user);
//        if(target==null){
//            return new Result<>(ResInfo.NOT_FOUND,null);
//        }
//        if(!user.getPassword().equals(target.getPassword())){
//            return new Result<>(ResInfo.AUTH_FAIL,null);
//        }
//        return new Result<>(ResInfo.SUCCESS,null);
    }

    @RequestMapping(value = "login",method = RequestMethod.DELETE)
    public Result logout(
            @RequestParam(required = false) String name
    ){
        return new Result<>(ResInfo.SUCCESS,"logout");
    }
}
