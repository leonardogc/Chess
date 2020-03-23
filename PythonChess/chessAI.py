import chess
import chess.svg


def heuristic(maxi_is_white):
    wp = len(board.pieces(chess.PAWN, chess.WHITE))
    bp = len(board.pieces(chess.PAWN, chess.BLACK))
    wn = len(board.pieces(chess.KNIGHT, chess.WHITE))
    bn = len(board.pieces(chess.KNIGHT, chess.BLACK))
    wb = len(board.pieces(chess.BISHOP, chess.WHITE))
    bb = len(board.pieces(chess.BISHOP, chess.BLACK))
    wr = len(board.pieces(chess.ROOK, chess.WHITE))
    br = len(board.pieces(chess.ROOK, chess.BLACK))
    wq = len(board.pieces(chess.QUEEN, chess.WHITE))
    bq = len(board.pieces(chess.QUEEN, chess.BLACK))

    material = 100 * (wp - bp) + 320 * (wn - bn) + 330 * (wb - bb) + 500 * (wr - br) + 900 * (wq - bq)

    if maxi_is_white:
        return material
    else:
        return -material


max_depth = 5


def minimax(maxi, depth):

    if depth >= max_depth:
        if board.is_game_over():
            if board.is_checkmate():
                if maxi:
                    return -1000000 + depth, None
                else:
                    return 1000000 - depth, None
            else:
                return 0, None
        else:
            if maxi:
                return heuristic(board.turn), None
            else:
                return heuristic(not board.turn), None

    if board.is_game_over():
        if board.is_checkmate():
            if maxi:
                return -1000000 + depth, None
            else:
                return 1000000 - depth, None
        else:
            return 0, None

    max_score = -10000000
    min_score = 10000000

    for move in board.legal_moves:
        board.push(move)

        result, _ = minimax(not maxi, depth + 1)

        if maxi:
            if result > max_score:
                max_score = result
        else:
            if result < min_score:
                min_score = result

        board.pop()

    if maxi:
        return max_score, None
    else:
        return min_score, None


player_color = chess.WHITE

board = chess.Board()

value = minimax(True, 0)

print(value)

'''while not board.is_game_over():
    print('----')

    print(board)

    if board.turn == player_color:

        for move in board.legal_moves:
            print(move)

        move = None

        while move not in board.legal_moves:
            user_input = input('Select a move: ')
            move = chess.Move.from_uci(user_input)

        board.push(move)

    else:
        minimax()'''
