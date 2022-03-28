package kaniaveha.aliaksei.citadel.service;

import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.apache.maven.shared.dependency.analyzer.asm.DependencyClassFileVisitor;
import org.apache.tomcat.util.bcel.classfile.ClassParser;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;

/** Gives class details based on bytecode */
@Service
public class ByteCodeEngineeringService {

  public ClassDefinition toClassDefinition(byte[] byteCode) {
    String className = getClassName(byteCode);
    Set<String> referencedClassNames = getReferencedClassNames(byteCode);
    referencedClassNames.remove(className);
    return new ClassDefinition(className, referencedClassNames);
  }

  private String getClassName(byte[] byteCode) {
    try {
      return new ClassParser(toInputStream(byteCode)).parse().getClassName();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private Set<String> getReferencedClassNames(byte[] byteCode) {
    DependencyClassFileVisitor dependencyClassFileVisitor = new DependencyClassFileVisitor();
    dependencyClassFileVisitor.visitClass(null, toInputStream(byteCode));
    return dependencyClassFileVisitor.getDependencies();
  }

  private ByteArrayInputStream toInputStream(byte[] byteCode) {
    return new ByteArrayInputStream(byteCode);
  }
}
