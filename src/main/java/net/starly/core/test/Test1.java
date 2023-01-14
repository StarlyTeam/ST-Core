package net.starly.core.test;

import net.starly.core.spl.StarlyPlugin;

public class Test1 extends StarlyPlugin {
    {
        register();

        System.out.println("객체 생성됨 ㅋ 111");
    }

    @Override
    public void onRegister() {
        System.out.println("로드 된거노 ㅋㅋ 111");
    }
    
    @Override
    public void onUnregister() {
        System.out.println("언로드 되어버림~~ ㅋㅋ 111");
    }
}
