# I'm AOP Loggers for Spring Boot

[![maven central](https://maven-badges.herokuapp.com/maven-central/com.github.henkelian/im-aop-loggers-spring-boot/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.github.henkelian/im-aop-loggers-spring-boot)
[![license](https://img.shields.io/badge/license-MIT-blue.png)](https://github.com/henkelian/im-aop-loggers-spring-boot/blob/master/LICENSE)
[![codecov](https://codecov.io/gh/henkelian/im-aop-loggers-spring-boot/branch/master/graph/badge.svg?token=ND15KBP0RI)](https://codecov.io/gh/henkelian/im-aop-loggers-spring-boot)

`I'm AOP Loggers for Spring Boot` is a handy and configurable set of annotation-based loggers for [Spring Boot](https://spring.io/projects/spring-boot) that can log every execution of a method when entering or exiting normally or abnormally, without you writing a single line of code using aspect-oriented programming (AOP).

| Annotation           | Usage |
|----------------------|-------|
| `@LogBefore`         | Log before entering the target method | 
| `@LogAfterReturning` | Log after leaving the target method normally | 
| `@LogAfterThrowing`  | Log after leaving the target method by throwing an exception |
| `@LogAround`         | Log before entering and after leaving the target method, regardless of leaving normally or throwing an exception |

---

## @LogBefore

Typical usage of `@LogBefore` annotation looks like the following code.

Annotated on `Class`:
```java
@Service
@LogBefore
public class FooService {

  public Foo accept(final Foo foo) {
    return foo;
  }
}
```

Annotated on `Method`:
```java
@Service
public class FooService {

  @LogBefore
  public Foo accept(final Foo foo) {
    return foo;
  }
}
```

This will log an `entering message` before executing the method.
```text
DEBUG 26356 --- [           main] im.aop.loggers.demo.foo.FooService       : Entering [Foo accept(Foo)] with parameters [foo=Foo[foo=abc]]
```

Log level and message template for `entering message` can be configured in `application.properties` using the following properties:
| Configuration Properties | Default Value | Description |
|--------------------------|---------------|-------------|
| `im.aop.loggers.entering-level` | DEBUG | Log Level for entering message |
| `im.aop.loggers.entering-message` | Entering [{method}] with parameters [{parameters}] | Entering message template |

Message template for `entering message` supports the following variables:
| Template Variable | Description | Values |
|-------------------|-------------|--------|
| `method` | Method signature | void foo(String) |
| `parameters` | Method parameters | foo=abc |

The logger can be further customized at `@LogBefore` annotation with the following attributes:
| Annotation Attribute | Description |
|----------------------|-------------|
| `declaringClass` | Class name used as Logger's category name |
| `level` | Log Level for entering message |
| `enteringMessage` | Entering message template |

Values configured with annotation attributes has `higher priority` over configuration properties.

---

### @LogAfterReturning

Typical usage of `@LogAfterReturning` annotation looks like the following code.

Annotated on `Class`:
```java
@Service
@LogAfterReturning
public class BarService {

  public Bar accept(Bar bar) {
    return bar;
  }
}
```

Annotated on `Method`:
```java
@Service
public class BarService {

  @LogAfterReturning
  public Bar accept(Bar bar) {
    return bar;
  }
}
```

This will log an `exited normally message` after exited the method normally.
```text
DEBUG 26356 --- [           main] im.aop.loggers.demo.bar.BarService       : [Bar accept(Bar)] exited normally with return value [Bar[bar=abc]]
```

Log level and message template for `exited normally message` can be configured in `application.properties` using the following properties:
| Configuration Properties | Default Value | Description |
|--------------------------|---------------|-------------|
| `im.aop.loggers.exited-level` | DEBUG | Log Level for exited normally message |
| `im.aop.loggers.exited-message` | [{method}] exited normally with return value [{return-value}] | Exited normally message template |

Message template for `exited normally message` supports the following variables:
| Template Variable | Description | Values |
|-------------------|-------------|--------|
| `method` | Method signature | void foo(String) |
| `return-value` | Method return value | abc |

The logger can be further customized at `@LogAfterReturning` annotation with the following attributes: 
| Annotation Attribute | Description |
|----------------------|-------------|
| `declaringClass` | Class name used as Logger's category name |
| `level` | Log Level for exited message |
| `exitedMessage` | Exited message template |

Values configured with annotation attributes has `higher priority` over configuration properties.

---

### @LogAfterThrowing

Typical usage of `@LogAfterThrowing` annotation looks like the following code.

Annotated on `Class`:
```java
@Service
@LogAfterThrowing
public class BazService {

  public Baz accept(Baz baz) {
    throw new IllegalArgumentException("Baz");
  }
}
```

Annotated on `Method`:
```java
@Service
public class BazService {

  @LogAfterThrowing
  public Baz accept(Baz baz) {
    throw new IllegalArgumentException("Baz");
  }
}
```

This will log an `exited abnormally message` after exited the method with an exception.
```text
ERROR 19036 --- [           main] im.aop.loggers.demo.baz.BazService       : [Baz accept(Baz)] exited abnormally with exception [type=IllegalArgumentException, message=Baz]
```

Log level, message template and ignored exceptions for `exited abnormally message` can be configured in `application.properties` using the following properties:
| Configuration Properties | Default Value | Description |
|--------------------------|---------------|-------------|
| `im.aop.loggers.exited-abnormally-level` | ERROR  | Log Level for exited abnormally message |
| `im.aop.loggers.exited-abnormally-message` | [{method}] exited abnormally with exception [{exception}] | Exited abnormally message template |
| `im.aop.loggers.ignore-exceptions` | | Exceptions that will be ignored by Logger |

Message template for `exited abnormally message` supports the following variables:
| Template Variable | Description | Values |
|-------------------|-------------|--------|
| `method` | Method signature | void foo(String) |
| `exception` | Thrown exception | type=IllegalArgumentException, message=Baz | `exited-abnormally-message` |

The logger can be further customized at `@LogAfterThrowing` annotation with the following attributes:
| Annotation Attribute | Description |
|----------------------|-------------|
| `declaringClass` | Class name used as Logger's category name |
| `level` | Log Level for exited abnormally message |
| `exitedAbnormallyMessage` | Exited abnormally message template |
| `ignoreExceptions` | Exceptions that will be ignored by Logger |

Values configured with annotation attributes has `higher priority` over configuration properties.

---

### @LogAround

Typical usage of `@LogAround` annotation looks like the following code:

Annotated on `Class`:
```java
@Service
@LogAround
public class QuxService {

  public Qux accept(Qux qux) {
    return qux;
  }
}
```

Annotated on `Method`:
```java
@Service
public class QuxService {

  @LogAround
  public Qux accept(Qux qux) {
    return qux;
  }
}
```

This will log
1. an `entering message` before executing the method
2. an `exited normally message` when exited the method normally
3. an `exited abnormally message` when exited the method with an exception
4. an `elapsed message` for the execution
5. an `elapsed warning message` when execution time exceeded the configured limit. `elapsed time limit` needs to be configured on `@LogAround` annotation individually to enable `elapsed warning message` for that method.
```text
INFO 19036 --- [           main] im.aop.loggers.demo.qux.QuxService       : Entering [Qux accept(Qux)] with parameters [qux=Qux[qux=abc]]
INFO 19036 --- [           main] im.aop.loggers.demo.qux.QuxService       : [Qux accept(Qux)] exited normally with return value [Qux[qux=abc]]
INFO 19036 --- [           main] im.aop.loggers.demo.qux.QuxService       : [Qux accept(Qux)] elapsed [PT0.01394S]
WARN 13248 --- [           main] im.aop.loggers.demo.qux.QuxService       : [Qux accept(Qux)] reached elapsed time limit [PT0.005S]
```

`@LogAround` shares the same configuration properties defined for `@LogBefore`, `@LogAfterReturning` and `@LogAfterThrowing` annotations.

Additionally, log level and message template for `elapsed message` and `elapsed warning message` can be configured in `application.properties` using the following properties:
| Configuration Properties | Default Value | Description |
|--------------------------|---------------|-------------|
| `im.aop.loggers.elapsed-level` | DEBUG | Log Level for elapsed message |
| `im.aop.loggers.elapsed-message` | [{method}] elapsed [{elapsed}] | Elapsed message template |
| `im.aop.loggers.elapsed-warning-level` | WARN  | Log Level for elapsed warning message |
| `im.aop.loggers.elapsed-warning-message` | [{method}] reached elapsed time limit [{elapsed-time-limit}] | Elapsed warning message template |

Message template for `entering message` supports the following variables:
| Template Variable | Description | Values |
|-------------------|-------------|--------|
| `method` | Method signature | void foo(String) | All |
| `elapsed` | Method elapsed time | PT0.01S | `elapsed-message` |

Message template for `entering warning message` supports the following variables:
| Template Variable | Description | Values |
|-------------------|-------------|--------|
| `elapsed-time-limit` | Method elapsed time limit | PT0.01S | `elapsed-warning-message` |

The logger can be further customized at `@LogAround` annotation with the following attributes:
| Annotation Attribute | Description |
|----------------------|-------------|
| `declaringClass` | Class name used as Logger's category name |
| `level` | Log Level for entering, exited normally and elapsed message |
| `enteringMessage` | Entering message template |
| `exitedMessage` | Exited message template |
| `exitedAbnormallyLevel` | Log level for exited abnormally message |
| `exitedAbnormallyMessage` | Exited abnormally message template |
| `ignoreExceptions` | Exceptions that will be ignored by Logger |
| `enteringMessage` | Elapsed message template |
| `elapsedWarningLevel` | Log level for elapsed warning message |
| `elapsedWarningMessage` | Elapsed warning message template |
| `elapsedTimeLimit` | Elapsed time limit to log elapsed warning message. |
| `elapsedTimeUnit` | Elapsed time unit |

Values configured with annotation attributes has `higher priority` over configuration properties.

---

# I'm AOP Loggers for Spring Boot Demo

A demo Spring Boot application for `I'm AOP Loggers for Spring Boot` is available [here](https://github.com/henkelian/im-aop-loggers-spring-boot-demo).
