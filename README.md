# ssm_crud

### 说在前面

​	本项目在 Mac OS 环境下开发测试，在 Windows 环境下仅做项目可运行测试，所以在 Win 下可能会有隐藏小 bug。一些已发现的 Mac 与 Win 之间有代码差异的 bug 会在注释中说明，比较大的差异有两个地方，一个在Maven 配置文件 pom.xml 中，另一个在 myBatis generator 的配置文件 mbg.xml 中，详见文件中的注释说明即可。



### 版本说明

+ 工具环境：Mac OS + Eclipse
+ 开发语言：Java 1.8
+ 后台开发：Spring 5 + SpringMVC + myBatis
+ 前端开发：Bootstrap 4 + jQuery
+ 项目管理：Maven
+ 数据库：MySQL 8
+ 服务器：Tomcat 9



### 功能点

1. 分页

2. 数据校验

   jquery前端校验+JSR303后端校验

3. ajax

4. Rest风格的URI；使用HTTP协议请求方式的动词，来表示对资源的操作（GET（查询），POST（新增），PUT（修改），DELETE（删除））



### 技术点

- 基础框架-ssm （SpringMVC+Spring+Mybatis）
- 数据库-MySQL
- 前端框架-bootstrap快速搭建简洁美观的界面
- 项目的依赖管理-Maven
- 分页-pagehelper
- 逆向工程-MyBatis Generator



### 基础环境搭建

1. 创建一个Maven工程

   settings.xml配置镜像，本地仓库位置，以及jdk版本

2. 引入项目依赖的jar包

   - spring
   - springmvc
   - mybatis
   - 数据库连接池，驱动包
   - 其他(jstl,servlet-api,junit)

3. 引入bootstrap前端框架

4. 编写ssm整合的关键配置文件

   安装插件Spring Tool Suite

   https://spring.io/tools3/sts/all

   先要安装groovy

   https://github.com/groovy/groovy-eclipse/wiki

   - web.xml,spring,springmvc,mybatis，使用mybatis的逆向工程生成对应的bean以及mapper

5. 测试mapper



### 查询

1. 访问index.jsp页面
2. index.jsp页面发送出查询员工列表请求
3. EmployeeController来接受请求，查出员工数据
4. 来到list.jsp页面进行展示

- URI:/emps

### 查询-ajax

1. index.jsp页面直接发送ajax请求进行员工分页数据的查询
2. 服务器将查出的数据，以json字符串的形式返回给浏览器
3. 浏览器收到js字符串，可以使用js对json进行解析，使用js通过dom增删改改变页面
4. 返回json。实现客户端的无关性

### 新增逻辑

1. 在index.jsp页面点击“新增”
2. 弹出新增对话框
3. 去数据库查询部门列表，显示在对话框中
4. 用户输入数据，并进行校验
   - jquery前端校验，ajax用户名重复校验，重要数据（后端校验（JSR303），唯一约束）
   - 
5. 完成保存

- URI:
- /emp/{id} GET 查询员工
- /emp POST 保存员工
- /emp/{id} PUT 修改员工
- /emp/{id} DELETE 删除员工

### 修改逻辑

1. 点击编辑
2. 弹出用户修改的模态框（显示用户信息）
3. 点击更新，完成用户修改

### 删除逻辑

1. 单个删除
   - URL:/emp/{id} DELETE
2. 删除多个
   + URL:/emp/{ids} DELETE
