package kaniaveha.aliaksei.citadel.service;

import kaniaveha.aliaksei.citadel.test.Toolbox;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ByteCodeEngineeringServiceTest {

  private final ByteCodeEngineeringService testee = new ByteCodeEngineeringService();

  private static final String sourceCode = """
            package test.dependencies;
            
            import java.io.File;
            import java.io.IOException;
            import java.nio.file.Path;
            import java.nio.file.Paths;
            import java.util.Collection;
            import java.util.LinkedList;
            import javax.tools.OptionChecker;
            import javax.tools.ToolProvider;
            
            class ClassWithDependencies extends LinkedList implements OptionChecker {
            
              private final ToolProvider classField = null;
            
              @Override
              public int isSupportedOption(String methodParameter) {
                return 42;
              }
              
              private File method(Collection[] methodArrayParameter) throws IOException {
                 new LinkedList().stream().map(this::methodRef).toList();
              
                 Path methodLocalVariable = null;
                 return methodLocalVariable.toFile();
              }
              
              private File methodRef(Object o) {
                return null;
              }
              
            }
            """;

  @Test
  void derivesClassDefinitionFromBytecode() {
    // given
    byte[] byteCode = Toolbox.compile(sourceCode);

    // when
    ClassDefinition actualClassDefinition = testee.toClassDefinition(byteCode);

    // then
    assertThat(actualClassDefinition, equalTo(new ClassDefinition(
            "test.dependencies.ClassWithDependencies",
            new HashSet<>(asList(
              "javax.tools.ToolProvider",               //class field
              "javax.tools.OptionChecker",              //an interface the class implements
              "java.util.LinkedList",                   //a class the class extends
              "java.lang.String",                       //method parameter
              "java.util.Collection",                   //method array parameter
              "java.io.File",                           //method return value
              "java.io.IOException",                    //method exception
              "java.nio.file.Path",                     //method local variable
              "java.lang.invoke.LambdaMetafactory",    //lambdas/streams stuff
              "java.lang.invoke.MethodHandles",        //lambdas/streams stuff
              "java.lang.invoke.MethodHandles$Lookup", //lambdas/streams stuff
              "java.util.stream.Stream",               //lambdas/streams stuff
              "(Ljava.lang.Object;)Ljava.lang.Object;",//lambdas/streams stuff
              "java.lang.Object"                       //method parameter
            ))
          )));
  }

}
