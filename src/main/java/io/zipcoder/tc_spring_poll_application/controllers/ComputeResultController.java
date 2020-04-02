package io.zipcoder.tc_spring_poll_application.controllers;

import dtos.OptionCount;
import dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.domain.Option;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.ManyToOne;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class ComputeResultController {

    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);
        Map<Option, Integer> count = new LinkedHashMap<>();
        List<OptionCount> opList = new LinkedList<>();
        Integer totalCount = 0;
        //TODO: Implement algorithm to count votes
        for(Vote v: allVotes){
            Option currOption = v.getOption();
            if(count.containsKey(currOption)){
                count.replace(currOption, count.get(currOption), count.get(currOption)+1);
            }else {
                count.put(currOption, 1);
            }
        }
        for(Map.Entry<Option, Integer> e: count.entrySet()){
            totalCount += e.getValue();
            OptionCount newOptionCount = new OptionCount();
            newOptionCount.setOptionId(e.getKey().getId());
            newOptionCount.setCount(e.getValue());
            opList.add(newOptionCount);
            voteResult.setTotalVotes(totalCount);
        }
        voteResult.setResults(opList);
        return new ResponseEntity<>(voteResult, HttpStatus.OK);
    }
}
