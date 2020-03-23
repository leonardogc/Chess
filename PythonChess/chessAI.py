import chess
import chess.svg
import time


def heuristic(board, maxi_is_white):

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


def minimax(board, maxi=True, depth=0, alpha=-10000000, beta=10000000, max_depth=6):

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
                return heuristic(board, board.turn), None
            else:
                return heuristic(board, not board.turn), None

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

    best_move = None

    for move in board.legal_moves:
        board.push(move)

        result, _ = minimax(board, not maxi, depth + 1, alpha, beta, max_depth)

        if maxi:
            if result > max_score:
                max_score = result
                if depth == 0:
                    best_move = move

            if result > alpha:
                alpha = result
        else:
            if result < min_score:
                min_score = result

            if result < beta:
                beta = result

        board.pop()

        if alpha >= beta:
            break

    if maxi:
        return max_score, best_move
    else:
        return min_score, None


def main():
    player_color = chess.BLACK

    board = chess.Board()

    while not board.is_game_over():
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
            start = time.time()
            _, move = minimax(board)
            end = time.time()

            board.push(move)

            print(end - start)


if __name__ == '__main__':
    main()

