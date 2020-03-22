import chess
import chess.svg

board = chess.Board()

while not board.is_game_over():
    print('----')

    for move in board.legal_moves:
        print(move)

    move = None

    while move not in board.legal_moves:
        user_input = input('Select a move: ')
        move = chess.Move.from_uci(user_input)

    board.push(move)

