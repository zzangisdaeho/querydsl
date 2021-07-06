package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;

    @PostConstruct
    //PostConstructor와 Transactional 어노테이션은 spring life cycle 문제에 의해 같이 넣지 못한다.
    //고로 외부 클래스로 만들어주어 호출하는 방식으로 데이터를 넣어준다
    public void init(){
        initMemberService.init();
    }


    @Component
    static class InitMemberService{

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                 Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                 em.persist(new Member("member"+i, i, selectedTeam));

            }
        }
    }
}
