## 树洞社区-TreeDongForum
供初学者，学习、交流使用，喜欢的话，恳请给个star(*❦ω❦)。

http://www.treedong.com/
## 技术栈
  1. SpringBoot框架。<br/>
  2. Thymeleaf模板引擎。<br/>
  3. 数据访问层：Mybatis。<br/>
  4. 数据库：MySql，Redis。<br/>
  5. 服务器：内置Tomcat。<br/>
  6. 前端相关:Jquery,JavaScript，Ajax，Layer等。<br/>
  7. 前端模板：借鉴fly社区模板。<br/>
  8. 文件上传：阿里云、UCloud对象存储。<br/>
  9. 短信验证：阿里云短信。<br/>
  10. 邮箱验证：腾讯邮箱。<br/>
  11. 开源在线 Markdown 编辑器：Editor.md。<br/>
  12. OAuth2授权登入（QQ、Github）<br/>
  13. 验证码：腾讯验证码
## 主要功能
**帖子相关**

  1. 发帖<br/>
  2. 编辑<br/>
  3. 点赞<br/>
  4. 收藏<br/>
  5. 回复[（支持楼中楼回复）](http://www.treedong.com/)<br/>
  6. 帖子分类<br/>
  7. 标签<br/>
  8. 置顶帖<br/>
  9. 精华帖<br/>
  10. 管理面板（支持加精、置顶、删除等操作）。
  
**用户相关**

  1. 登录（五大登录方式）<br/>
  2. 注册<br/>
  3. 账号体系(绑定账户)（手机号、邮箱号、QQ、Github四合一）<br/>
  4. 上传头像(支持裁剪)<br/>
  5. 积分策略<br/>
  6. 等级晋升<br/>
  7. 消息通知<br/>
  8. 个人主页<br/>
  9. 帖子、收藏管理<br/>
  10. 账户中心<br/>
  11. 更新个人资料
  
**后台管理**

  1. 发布公告<br/>
  2. 管理面板（支持查看、加精、置顶、删除操作）。
  
**更多功能**

  1. 搜索<br/>
  2. 排序<br/>
  3. 验证码-防灌水、攻击
##快速运行
1.安装必备工具
  JDK，Maven
  
2.克隆代码到本地

3.根据提示与说明，编辑resources目录下的application.properties文件。

4.运行命令创建数据库脚本<br/>
```mvn clean compile flyway:migrate -Pdev```

5.编辑resources目录下的generatorConfig.xml文件，配置数据库相关信息（只需修改数据库链接、用户名、密码）。

6.访问项目<br/>
```http://localhost:8887/```

##目录结构
```
    ├─com.tree.community        应用目录
    │  ├─advice                 异常处理
    │  ├─cache                  热门话题和标签相关
    │  ├─config                 配置类
    │  ├─controller             控制层
    │  ├─dto                    数据传输层
    │  ├─enums                  枚举类
    │  ├─exception              自定义异常
    │  ├─interceptor            拦截器
    │  ├─mapper                 对象持久化映射层
    │  ├─model                  映射数据库实体类
    │  ├─provider               提供类
    │  ├─schedule               定时任务
    │  ├─service                业务逻辑层
    │  ├─util                   工具类
```
##更多链接
###联系我们
树洞社区官方QQ：2435663184

官方交流社区：http://www.treedong.com/
###资料
[Spring 文档](https://spring.io/guides)<br/>
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)<br/>
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)<br/>
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)<br/>
[Markdown 插件](http://editor.md.ipandao.com/)
###工具
[Git](https://git-scm.com/download)<br/>
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)<br/>
[Lombok](https://www.projectlombok.org/)<br/>
[MyBatis Generator](http://mybatis.org/generator/)


