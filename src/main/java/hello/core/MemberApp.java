package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

/*
 *  MemberService 기능 테스트를 위한 클라이언트
 */
public class MemberApp {
    //psvm + tab
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl(memberRepository);
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);

        //soutv + tab
        System.out.println("new member = " + findMember.getName());
        System.out.println("find Member = " + member.getName());

    }
}
