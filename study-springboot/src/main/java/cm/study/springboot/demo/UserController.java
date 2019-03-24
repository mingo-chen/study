package cm.study.springboot.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "user模块", tags = "USER")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * curl -v http://localhost:8080/test
     * @return
     */
    @ApiOperation(value = "测试服务是否正常", notes = "服务正常返回hello word; <br/>其它形式返回均为异常状态",
            produces = "application/json", consumes = "application/json", protocols="https")
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public Mono<String> testMono() {
        return userService.test();
    }

    /**
     * curl -v -d "{\"id\":10,\"name\":\"cm\"}" -H "content-type:application/json" http://localhost:8080/addUser
     * curl -v -d '{"id":10,"name":"cm"}' -H "content-type:application/json" http://localhost:8080/addUser
     */
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public Flux<UserBean> add(@RequestBody Flux<UserBean> users) {
        return userService.createOrUpdate(users);
    }

    /**
     * curl http://localhost:8080/getUser/1  -- 查询存在的数据
     * curl http://localhost:8080/getUser/10 -- 查询不存在的数据
     */
    @RequestMapping(value = "getUser/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public Mono<UserBean> getUser(@PathVariable int id) {
        return userService.getUserBeanById(id);
    }

    /**
     * curl -v -X POST http://localhost:8080/deleteUser/10
     * curl -v -d "" http://localhost:8080/deleteUser/10
     */
    @RequestMapping(value = "deleteUser/{id}", method = RequestMethod.POST)
    public Mono<UserBean> delete(@PathVariable int id) {
        return userService.deleteById(id);
    }
}
