package com.example.ch13_interface;

public interface DiamondProblem {
    interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }

    interface B extends A {
        default void hello() {
            System.out.println("Hello from B");
        }
    }

    public class C implements A, B {
        public static void main(String[] args) {
            new C().hello(); // 뭐가 출력될까?
        }
    }

    /*
     * 상속 규칙
     * 1. 구현된 클래스가 이긴다.
     * 2. 서브 인터페이스가 이긴다.
     * 3. 디폴트 메서드 우선순위가 결정되지 않았다면 명시적으로 디폴트 메서드를 오버라이드해야한다.
     * */
    public class D implements A {
    }

    public class E extends D implements B, A {
        public static void main(String[] args) {
            new E().hello(); //여기서는 1번 구현되지 않았으므로 패스, 2번에서 서브인터페이승니 B가 이긴다.
        }
    }
}

interface DiamondProblem2 {
    public interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }

    public interface B {
        default void hello() {
            System.out.println("Hello from B");
        }
    }

    //    public class C implements B, A { }
    //여기서는 3번규칙이 적용된다. 지금은 컴파일에러 발생한다.

    public class C implements B, A {
        public void hello() {
            B.super.hello();
        }
    }
}


interface DiamondProblem3 {
    public interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
   }
   public interface B extends A {}
    public interface C extends A {
        // 여기서는 선택권이 default 메서드인 A.hello()를 호출
//        void hello(); // 여기서는 서브 인터페이스므로 주석을 해제하면 구현해야한다.
    }
    public class D implements B, C {
        public static void main(String[] args) {
            new D().hello();
        }
    }
}

/*
* 1. 클래스, 슈퍼클래스에서 정의한 메서드가 디폴트 메소드보다 우선권
* 2. 서브인터페이스가 우선권
* 여기서는 디폴트 메서드를 오버라이드하고 호출해야함.
* */