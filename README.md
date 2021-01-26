# fork 说明
为了支持 spark-operator-on-k8s ，即在 k8s 中部署 spark-sql ，所以在开源 yaooqinn/kyuubi 基础上，主要增加了 k8s session manager 和 k8s session implement 功能。

与开源实现的主要区别如下：

开源 kyuubi 在服务启动时，会开启一个内嵌的 zk 服务，在接收到 jdbc 请求之后，为用户启动Progress (通过 ProgressBuilder)，实际上在Progress 内通过 spark-submit 提交了一个spark  application，这个 application 的入口是 sql engine 的 main函数（本质上是一个 spark thrift server）。这个 spark application 的host 和 port 会注册在内嵌 zk 服务中，在下次收到相同用户的 jdbc 连接时，查询 zk ，如果已存在相同用户的 spark sql application ，则可复用。

但是，如果使用 spark operator on k8s 提交 SparkApplication ，则无法采用类似方式提交和管理 spark sql 应用。所以，主要的变化是，增加了 k8sSessionManager 和 k8sSessionImpl，在收到用户的 jdbc 连接请求之后，通过定制 SparkCtrlOp 服务提供的 http 接口提交 spark application，SparkCtrlOp 内部则调用 SparkOperator 接口实现 SparkApplication 应用任务的实际启动。在下次收到相同用户的 jdbc 连接时，通过 SparkCtrl 查询当前用户的 Spark Sql Application，实现 Spark Sql Application（即 Spark Thrift Server）的复用。

# Kyuubi
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://tokei.rs/b1/github/yaooqinn/kyuubi)](https://github.com/yaooqinn/kyuubi)
![GitHub top language](https://img.shields.io/github/languages/top/yaooqinn/kyuubi)
[![GitHub release](https://img.shields.io/github/release/yaooqinn/kyuubi.svg)](https://github.com/yaooqinn/kyuubi/releases)
[![codecov](https://codecov.io/gh/yaooqinn/kyuubi/branch/master/graph/badge.svg)](https://codecov.io/gh/yaooqinn/kyuubi)
[![HitCount](http://hits.dwyl.io/yaooqinn/kyuubi.svg)](http://hits.dwyl.io/yaooqinn/kyuubi)
[![Travis](https://travis-ci.org/yaooqinn/kyuubi.svg?branch=master)](https://travis-ci.org/yaooqinn/kyuubi)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/yaooqinn/kyuubi/Kyuubi/master?style=plastic)
[![Documentation Status](https://readthedocs.org/projects/kyuubi/badge/?version=latest)](https://kyuubi.readthedocs.io/en/latest/?badge=latest)
[![DepShield Badge](https://depshield.sonatype.org/badges/yaooqinn/kyuubi/depshield.svg)](https://depshield.github.io)

Kyuubi is a high-performance universal JDBC and SQL execution engine, built on top of [Apache Spark](http://spark.apache.org).
The goal of Kyuubi is to facilitate users to handle big data like ordinary data.

It provides a standardized JDBC interface with easy-to-use data access in big data scenarios.
End-users can focus on developing their own business systems and mining data value without having to be aware of the underlying big data platform (compute engines, storage services, metadata management, etc.).

Kyuubi relies on Apache Spark to provide high-performance data query capabilities,
and every improvement in the engine's capabilities can help Kyuubi's performance make a qualitative leap.
In addition, Kyuubi improves ad-hoc responsiveness through the engine caching,
and enhances concurrency through horizontal scaling and load balancing.
It provides complete authentication and authentication services to ensure data and metadata security.
It provides robust high availability and load balancing to help you guarantee the SLA commitment.
It provides a two-level elastic resource management architecture to effectively improve resource utilization while covering the performance and response requirements of all scenarios including interactive,
or batch processing and point queries, or full table scans.
It embraces Spark and builds an ecosystem on top of it,
which allows Kyuubi to quickly expand its existing ecosystem and introduce new features,
such as cloud-native support and `Data Lake/Lake House` support.

Kyuubi's vision is to build on top of Apache Spark and Data Lake technologies to unify the portal and become an ideal data lake management platform.
It can support data processing e.g. ETL, and analytics e.g. BI in a pure SQL way.
All workloads can be done on one platform, using one copy of data, with one SQL interface.

## Online Documentation

Since Kyuubi 1.0.0, the Kyuubi online documentation is hosted by [https://readthedocs.org/](https://readthedocs.org/).
You can find the specific version of Kyuubi documentation as listed below.

- [master/latest](https://kyuubi.readthedocs.io/en/latest/)
- [stable](https://kyuubi.readthedocs.io/en/stable/)
- [v1.0.2](https://kyuubi.readthedocs.io/en/v1.0.2/)
- [v1.0.1](https://kyuubi.readthedocs.io/en/v1.0.1/)
- [v1.0.0](https://kyuubi.readthedocs.io/en/v1.0.0/)

For 0.8 and earlier versions, please check the [project docs folder](https://github.com/yaooqinn/kyuubi/tree/branch-0.7/docs) directly.

## Quick Start

Ready? [Getting Started](https://kyuubi.readthedocs.io/en/latest/quick_start/quick_start.html) with Kyuubi.

## Contributing

All bits of help are welcome. You can make various types of contributions to Kyuubi, including the following but not limited to,

- Help new users in chat channel or share your success stories w/ us - [![Gitter](https://badges.gitter.im/kyuubi-on-spark/Lobby.svg)](https://gitter.im/kyuubi-on-spark/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
- Improve Documentation - [![Documentation Status](https://readthedocs.org/projects/kyuubi/badge/?version=latest)](https://kyuubi.readthedocs.io/en/latest/?badge=latest)
- Test releases - [![GitHub release](https://img.shields.io/github/release/yaooqinn/kyuubi.svg)](https://github.com/yaooqinn/kyuubi/releases)
- Improve test coverage - [![codecov](https://codecov.io/gh/yaooqinn/kyuubi/branch/master/graph/badge.svg)](https://codecov.io/gh/yaooqinn/kyuubi)
- Report bugs and better help developers to reproduce
- Review changes
- Make a pull request
- Promote to others
- Click the star button if you like this project

## Aside

The project took its name from a character of a popular Japanese manga - `Naruto`.
The character is named `Kyuubi Kitsune/Kurama`, which is a nine-tailed fox in mythology.
`Kyuubi` spread the power and spirit of fire, which is used here to represent the powerful [Apache Spark](http://spark.apache.org).
It's nine tails stands for end-to end multi-tenancy support of this project.
