package hello.core.member;

public interface MemberRepository {

    void save(Member member);
    void search(Member member);
}
