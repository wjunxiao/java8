package com.sytx.demo;


import com.sytx.demo.pojo.Person;
import com.sytx.demo.pojo.Status;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
    enum Status{
        FREE,BUSY,VOCATION
    }
//https://blog.csdn.net/liudongdong0909/article/details/77429875
    /**
     * 创建stream方法
     * 1、通过collection的stream方法，串行
     * 2、通过parallelStream()，并行
     * 3、Stream类of方法
     * 4、迭代 Stream.iterate()方法
     * */
    @Test
    public void test(){
//        1

    }
    /**
     * 映射
     * 将元素转换成其他形式或者提取信息。
     * 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
     */
    @Test
    public void streamMap(){
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd");
//        将流中每个元素映射到map函数中，每个元素执行这个函数再返回
//        list.stream().map((e)->e.toUpperCase()).forEach(System.out::println);

//        flatMap，接收一个函数作为参数，将流中的每个值换成一个流，然后把所有流连接一个流
        list.stream().flatMap((e)->filterCharacter(e)).forEach(System.out::println);

        String[] arrs = {"java8", "is", "easy", "to", "use"};

//       构建一数组所有非重复字符
        List<String> distinctStr = Arrays.stream(arrs)
                .map(str->str.split(""))//映射Stream<String[]>
                .flatMap(Arrays::stream) //扁平化为Stream<String>
                .distinct()
                .collect(Collectors.toList());
        distinctStr.forEach(System.out::println);
    }

    /**
     * stored排序
     * 自然排序、定制排序
     * */
    @Test
    public void stramStored(){

       List<Person> persons= testData();
//定制排序按照年龄排序
//        persons.stream().sorted((e1,e2)->{
//            if(e1.Age == e2.Age){
//                return 0;
//            }else if(e1.Age > e2.Age){
//                return 1;
//            }
//            else {
//                return  -1;
//            }
//        }).collect(Collectors.toList()).forEach((e)->{
//            System.out.println(e.Name+" "+e.Age);
//        });
        persons.stream().sorted((e1,e2)->Integer.compare(e1.Age,e2.Age)).collect(Collectors.toList()).forEach((e)->{
            System.out.println(e.Name+" "+e.Age);
        });
// .allMatch检查是否匹配所有元素,是否所有名字叫张三
        boolean b= persons.stream().allMatch((e)->"张三".equals(e.Name));
        System.out.println(b);
//  .anyMatch检查是否至少匹配所有元素
        b= persons.stream().anyMatch((e)->"dd".equals(e.Name));
        System.out.println(b);
//  .noneMatch检查是否没有匹配所有元素
        b= persons.stream().noneMatch((e)-> com.sytx.demo.pojo.Status.VOCATION.equals(e.Status));
        System.out.println(b);

//  .max返回流中最大值
      Optional<Person> maxAgePerson= persons.stream().max((e1, e2)->Double.compare(e1.Age,e2.Age));

      Optional<Person> person1 = persons.stream().collect(Collectors.maxBy((x,y)->Double.compare(x.Age,y.Age)));
//      isPresent：值是否存在
      if(maxAgePerson.isPresent()){
          System.out.println(maxAgePerson.get().Name);
      }
      if(person1.isPresent()){
          System.out.println(person1.get().Name);
      }

    }
    /**
     * .reduce归约
     * */
    @Test
    public void streamReduce(){
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        reduce 返回可能结果为空
//        Integer sum = list.stream().reduce(0,(x,y)->x+y);
        Optional<Integer> sum = list.stream().reduce(Integer::sum);
        System.out.println(sum);
        List<Person> people = testData();
//        Collectors.toList() 将流转换成list
        List<String> nameList = people.stream()
                .map(person -> person.Name)
                .distinct()
                .collect(Collectors.toList());

        nameList.forEach(System.out::println);

        /***
         * reduce 没有初始值返回optional
         * */
//        统计年龄小于10岁，总和
        Optional<Integer> ageCount = people.stream()
                .map(person -> person.Age)
                .filter(age->age < 20)
                .reduce((age1,age2)->age1+age2);
//        空返回0，防止空指针异常
        System.out.println("age"+ ageCount.orElse(0));

        Map<Integer,String> map = new HashMap<>();
        map.put(2089,"呵呵");
        map.put(2087,"呵呵1");
        map.put(2086,"呵呵2");

        String name = Optional.ofNullable(map.get(22)).orElse("无") ;
        System.out.println(name);

    }


private List<Person> testData(){
    List<Person> persons = Arrays.asList(
            new Person("张三", "男", 76, com.sytx.demo.pojo.Status.BUSY),
            new Person("李四", "女", 12, com.sytx.demo.pojo.Status.BUSY),
            new Person("王五", "男", 35,com.sytx.demo.pojo.Status.BUSY),
            new Person("赵六", "男", 3, com.sytx.demo.pojo.Status.FREE),
            new Person("钱七", "男", 56, com.sytx.demo.pojo.Status.BUSY),
            new Person("翠花", "女", 34, com.sytx.demo.pojo.Status.BUSY),
            new Person("翠花", "女", 34, com.sytx.demo.pojo.Status.BUSY),
            new Person("翠花", "女", 34, com.sytx.demo.pojo.Status.BUSY)
    );
    return  persons;
}
    public Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();

        for (Character ch: str.toCharArray()) {
            list.add(ch);
        }
        return  list.stream();
    }
}
