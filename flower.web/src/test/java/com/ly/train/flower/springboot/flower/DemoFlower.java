/**
 * Copyright © 2019 同程艺龙 (zhihui.li@ly.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.ly.train.flower.springboot.flower;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ly.flower.web.spring.FlowerController;
import com.ly.train.flower.common.annotation.Flower;
import com.ly.train.flower.common.service.container.ServiceFactory;
import com.ly.train.flower.common.service.container.ServiceFlow;
import com.ly.train.flower.common.service.impl.AggregateService;
import com.ly.train.flower.springboot.model.User;
import com.ly.train.flower.springboot.service.HeadService;
import com.ly.train.flower.springboot.service.UserService;
import com.ly.train.flower.springboot.service.UserService2;

/**
 * @author leeyazhou
 *
 */
@Flower(value = "flower", serviceName = "HeadService")
@RequestMapping("/flower/")
@RestController
public class DemoFlower extends FlowerController {


  @RequestMapping("test")
  public void test(User user, HttpServletRequest req) throws IOException {
    doProcess(user, req);
    logger.info("请求参数：{}", user);
  }

  @Override
  public void buildFlower() {
    ServiceFlow serviceFlow = ServiceFlow.getOrCreate(getFlowName());
    ServiceFactory.registerService("aggressGateService1", AggregateService.class);

    serviceFlow.buildFlow(HeadService.class, UserService.class);
    serviceFlow.buildFlow(HeadService.class, UserService2.class);
    serviceFlow.buildFlow("UserService", "aggressGateService1");
    serviceFlow.buildFlow("UserService2", "aggressGateService1");
    serviceFlow.buildFlow("aggressGateService1", "EndService");
  }

}
