package com.pl.premier_zone.player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
// contains the business logic of managing the data in the player class 
@Component
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository; 
    }

    //there is no "list of players" living inside the repository- the repo just knows how to query for them

    public List<Player> getPlayers(){
        return playerRepository.findAll();
        
    }

    //So yes — the way filter works is:
    //Take each element from the stream one by one.
    //Assign it to the lambda parameter.
    //Run your condition.
    //Keep or drop the element based on the result.
    //.collect is a terminal operation. 
    //.filter() in a stream sets up a rule, but it won’t actually process anything until a terminal operation like .collect() triggers the whole stream to run.
    //Stream<T> in Java does not mean “a single object” — it means:
    //A sequence of elements of type T that you can process one by one in a pipeline.
    //Yes — without .stream() in your code, the .filter(...) would crash because filter is not a method that exists on a List.

    public List<Player> getPlayersFromTeam(String teamName) {
        return playerRepository.findAll().stream().filter( player -> teamName.equals(player.getTeam())).collect(Collectors.toList());
             }
             
    public List<Player> getPlayerByName(String searchText){
        return playerRepository.findAll().stream().filter( player -> player.getName().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    public List<Player> getPlayersByPosition(String searchText){
        return playerRepository.findAll().stream().filter(player -> player.getPos().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }
    
    public List<Player> getPlayersByNation(String searchText){
        return playerRepository.findAll().stream().filter(player -> player.getNation().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    public List<Player> getPlayersByTeamAndPosition(String team, String position){
        return playerRepository.findAll().stream().filter(player -> team.equals(player.getTeam()) && position.equals(player.getPos())).collect(Collectors.toList());
    }

    public Player addPlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    public Player updatePlayer(Player updatedPlayer) {

        Optional<Player> existingPlayer = playerRepository.findByName(updatedPlayer.getName());

        if(existingPlayer.isPresent()) {
            Player playerToUpdate = existingPlayer.get();
            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setTeam(updatedPlayer.getTeam());
            playerToUpdate.setNation(updatedPlayer.getNation());
            playerToUpdate.setPos(updatedPlayer.getPos());

            playerRepository.save(playerToUpdate);
            return playerToUpdate;
        }
        return null; 
    }
    //@Transactional ensures your delete operation is all-or-nothing, keeping your database from ending up in a broken or half-updated state
    @Transactional
    public void deletePlayer(String playerName){
        playerRepository.deleteByName(playerName);
    }
}
