JInjector
=========

JInjector is a very simple manual injector for Java and GWT applications.

# Creating the JInjector instance

First you need an instance of the injector. You can get one simply instantiating the JInjector class.

    JInjector myInjector = new JInjector();
    
## Named injectors
You can create named injectors using the static JInjector.createNamed(...) method like this:

    JInjector myInjector = JInjector.createNamed("MyInjector");
    
After this you can get this named injector instance from anywhere in your code like this:

    JInjector myInjector = JInjector.GetInjector("MyInjector");
    
## Package injectors
Using named injectors you can also create package injectors. Assume that your code is in the package: org.myproject. You can register a named injector for this packaged like this:

    JInjector pkgInjector = JInjector.createNamed("org.myproject");
    
After this you can access this injector instance from any class which is under the org.myproject package or in one of it's sub package. Assume thet you have a class: MyClass in package: org.myproject.utils. You can access the previously registered injector like this:

    JInjector pkgInjector = JInjector.getPackageInjector();
    
You can also register an injector with name: "org". In this case this injector will be available calling the JInjector.getPackageInjector() under any class which is in the org pkg or in one of it's subpackage except classes in org.myproject package or in one of its subpackage because we registered a different injector for this package. Th JInjector.getPackageInjector() method is always returns the injector which matches the caller class's packages longest. 
    
# Registering instances
    
At some point when you application starts up you need to register your instances

    myInjector.register(MyServiceInterface.class, new MyServiceImpl());
    
This kind of registration creates a singleton registration. You always get back the same instance which you registered.

You can also register providers like this:

    myInjector.register(MyServiceInterface.class, new Provider<MyServiceInterface>() {
    
        public MyServiceInterface get() {
            if (someCondition) {
                return new MyServiceImpl1();
            } else {
                return new MyServiceImpl2();
            }
        }
    
    });
    
This kind of registration can be used when you want to return a new instance on every get call, or if you want to return different instances based on some conditions.

# Getting instances

After you registered all the services and instances you need, you can get back like this:

    MyServiceInterface service = myInjector.get(MyServiceInterface.class);
    
This returns the the registered instance of the MyServiceInterface class. Using the get method, the MyServiceInterface class must be already registered.

You can also get providers like this:

    Provider<MyServiceInterface> serviceProvider = myInjector.getProvider(MyServiceInterface.class);
    
This method does not require the MyServiceInterface to be registered. You can register the MyServiceInterface bind later. You can get the instance from the provider using the get() method:

    MyServiceInterface service = serviceProvider.get();
    
At this point, the MySrviceInterface bind must be registered.
