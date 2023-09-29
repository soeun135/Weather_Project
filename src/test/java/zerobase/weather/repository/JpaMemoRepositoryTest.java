package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {

//    @Autowired
//    JpaMemoRepository jpaMemoRepository;
//
//    @Test
//    void insertMemoTest() {
//        //given
//        Memo newMemo = new Memo(2, "hi");
//
//        //when
//        jpaMemoRepository.save(newMemo);
//
//        //then
//        List<Memo> memoList = jpaMemoRepository.findAll();
//        assertTrue(memoList.size() > 0);
//     }
//
//    @Test
//    void findByIdTest() {
//        //given
//        Memo newMemo = new Memo(190, "나는 소은");
//
//        //when
//        Memo memo = jpaMemoRepository.save(newMemo);
//        System.out.println(memo.getId());
//        //then
//        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
//        assertEquals("나는 소은", result.get().getText());
//    }
}