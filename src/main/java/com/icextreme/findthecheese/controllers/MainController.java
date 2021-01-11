package com.icextreme.findthecheese.controllers;

import com.icextreme.findthecheese.model.Game;
import com.icextreme.findthecheese.wrappers.ApiBoardWrapper;
import com.icextreme.findthecheese.wrappers.ApiGameWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the controller for the REST API.
 */
@RestController
public class MainController {
    List<Game> games = new ArrayList<>();
    List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
    private AtomicInteger nextId = new AtomicInteger();

    @GetMapping("/hello")
    public String getHelloMessage() {
        return "Hello World!";
    }

    @GetMapping("/api/about")
    public String getName() {
        return "IceXtreme";
    }

    @GetMapping("/api/games")
    public List<ApiGameWrapper> getApiGameWrappers() {
        return apiGameWrappers;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/api/games")
    public ApiGameWrapper createNewGame() {
        Game game = new Game();
        games.add(game);

        ApiGameWrapper apiGameWrapper = ApiGameWrapper.makeFromGame(game, nextId.incrementAndGet() - 1);
        apiGameWrappers.add(apiGameWrapper);

        return apiGameWrapper;
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getGame(@PathVariable("id") int gameId) {
        for (ApiGameWrapper gameWrapper : apiGameWrappers) {
            if (gameWrapper.gameNumber == gameId) {
                return gameWrapper;
            }
        }
        throw new NotFoundException();
    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getGameBoard(@PathVariable("id") int gameId) {
        try {
            return ApiBoardWrapper.makeFromGame(games.get(gameId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED, reason = "Post move accepted")
    @PostMapping("/api/games/{id}/moves")
    public void postMoveToGameBoard(
            @PathVariable("id") int gameId,
            @RequestBody String move
    ) {
        String input;

        checkIfGameDoesNotExist(gameId);

        Game game = games.get(gameId);

        switch (move) {
            case "MOVE_UP":
                input = "w";
                checkAndMoveMouse(input, game);
                break;
            case "MOVE_DOWN":
                input = "s";
                checkAndMoveMouse(input, game);
                break;
            case "MOVE_LEFT":
                input = "a";
                checkAndMoveMouse(input, game);
                break;
            case "MOVE_RIGHT":
                input = "d";
                checkAndMoveMouse(input, game);
                break;
            case "MOVE_CATS":
                game.getBoard().moveCats();
                break;
            default:
                throw new BadRequestException();
        }

        refreshGameWrapper(gameId, game);
    }


    @ResponseStatus(value = HttpStatus.ACCEPTED, reason = "Post cheat accepted")
    @PostMapping("/api/games/{id}/cheatstate")
    public void postCheatToGameBoard(
            @PathVariable("id") int gameId,
            @RequestBody String move
    ) {
        checkIfGameDoesNotExist(gameId);

        Game game = games.get(gameId);

        switch (move) {
            case "1_CHEESE":
                game.getBoard().cheeseCheat();
                break;
            case "SHOW_ALL":
                game.getBoard().mapCheat();
                break;
            default:
                throw new BadRequestException();
        }

        refreshGameWrapper(gameId, game);
    }

    private void checkIfGameDoesNotExist(@PathVariable("id") int gameId) {
        try {
            Game game = games.get(gameId);
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    private void checkAndMoveMouse(String input, Game game) {
        if (game.getBoard().mouseMoveIntoWall(input)) {
            throw new BadRequestException();
        }
        game.getBoard().moveMouse(input);
    }

    private void refreshGameWrapper(@PathVariable("id") int gameId, Game game) {
        ApiGameWrapper apiGameWrapper = ApiGameWrapper.makeFromGame(game, gameId);
        apiGameWrappers.set(gameId, apiGameWrapper);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public static class NotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
    public static class BadRequestException extends RuntimeException {
    }
}
