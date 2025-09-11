package com.example.wuaicode.service;

import com.example.wuaicode.model.dto.app.AppQueryRequest;
import com.example.wuaicode.model.entity.App;
import com.example.wuaicode.model.entity.User;
import com.example.wuaicode.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AppService extends IService<App> {
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    String deployApp(Long appId, User loginUser);


    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);
}
