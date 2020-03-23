import chess
import time

king_score = 200000
queen_score = 9750
rook_score = 5000
knight_score = 3250
bishop_score = 3350
pawn_score = 1000

bishop_pair = 300

knight_adj = [-200, -160, -120, -80, -40, 0, 40, 80, 120]
rook_adj = [150, 120, 90, 60, 30, 0, -30, -60, -90]

king_board_middle = [20, 30, 10, 0, 0, 10, 30, 20,
                     20, 20, 0, 0, 0, 0, 20, 20,
                     -10, -20, -20, -20, -20, -20, -20, -10,
                     -20, -30, -30, -40, -40, -30, -30, -20,
                     -30, -40, -40, -50, -50, -40, -40, -30,
                     -30, -40, -40, -50, -50, -40, -40, -30,
                     -30, -40, -40, -50, -50, -40, -40, -30,
                     -30, -40, -40, -50, -50, -40, -40, -30]

king_board_end = [-50, -30, -30, -30, -30, -30, -30, -50,
                  -30, -30, 0, 0, 0, 0, -30, -30,
                  -30, -10, 20, 30, 30, 20, -10, -30,
                  -30, -10, 30, 40, 40, 30, -10, -30,
                  -30, -10, 30, 40, 40, 30, -10, -30,
                  -30, -10, 20, 30, 30, 20, -10, -30,
                  -30, -20, -10, 0, 0, -10, -20, -30,
                  -50, -40, -30, -20, -20, -30, -40, -50]

queen_board = [-20, -10, -10, -5, -5, -10, -10, -20,
               -10, 0, 5, 0, 0, 0, 0, -10,
               -10, 5, 5, 5, 5, 5, 0, -10,
               0, 0, 5, 5, 5, 5, 0, -5,
               -5, 0, 5, 5, 5, 5, 0, -5,
               -10, 0, 5, 5, 5, 5, 0, -10,
               -10, 0, 0, 0, 0, 0, 0, -10,
               -20, -10, -10, -5, -5, -10, -10, -20]

rook_board = [0, 0, 0, 5, 5, 0, 0, 0,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              -5, 0, 0, 0, 0, 0, 0, -5,
              5, 10, 10, 10, 10, 10, 10, 5,
              0, 0, 0, 0, 0, 0, 0, 0]

knight_board = [-50, -40, -30, -30, -30, -30, -40, -50,
                -40, -20, 0, 5, 5, 0, -20, -40,
                -30, 5, 10, 15, 15, 10, 5, -30,
                -30, 0, 15, 20, 20, 15, 0, -30,
                -30, 5, 15, 20, 20, 15, 5, -30,
                -30, 0, 10, 15, 15, 10, 0, -30,
                -40, -20, 0, 0, 0, 0, -20, -40,
                -50, -40, -30, -30, -30, -30, -40, -50]

bishop_board = [-20, -10, -10, -10, -10, -10, -10, -20,
                -10, 5, 0, 0, 0, 0, 5, -10,
                -10, 10, 10, 10, 10, 10, 10, -10,
                -10, 0, 10, 10, 10, 10, 0, -10,
                -10, 5, 5, 10, 10, 5, 5, -10,
                -10, 0, 5, 10, 10, 5, 0, -10,
                -10, 0, 0, 0, 0, 0, 0, -10,
                -20, -10, -10, -10, -10, -10, -10, -20]

pawn_board = [0, 0, 0, 0, 0, 0, 0, 0,
              5, 10, 10, -20, -20, 10, 10, 5,
              5, -5, -10, 0, 0, -10, -5, 5,
              0, 0, 0, 20, 20, 0, 0, 0,
              5, 5, 10, 25, 25, 10, 5, 5,
              10, 10, 20, 30, 30, 20, 10, 10,
              50, 50, 50, 50, 50, 50, 50, 50,
              60, 60, 60, 60, 60, 60, 60, 60]


def heuristic(board, maxi_is_white):
    wp = board.pieces(chess.PAWN, chess.WHITE)
    wn = board.pieces(chess.KNIGHT, chess.WHITE)
    wb = board.pieces(chess.BISHOP, chess.WHITE)
    wr = board.pieces(chess.ROOK, chess.WHITE)
    wq = board.pieces(chess.QUEEN, chess.WHITE)
    wk = board.pieces(chess.KING, chess.WHITE)

    bp = board.pieces(chess.PAWN, chess.BLACK)
    bn = board.pieces(chess.KNIGHT, chess.BLACK)
    bb = board.pieces(chess.BISHOP, chess.BLACK)
    br = board.pieces(chess.ROOK, chess.BLACK)
    bq = board.pieces(chess.QUEEN, chess.BLACK)
    bk = board.pieces(chess.KING, chess.BLACK)

    white_score = 0
    black_score = 0

    white_score += len(wp)*pawn_score + len(wn)*knight_score + len(wb)*bishop_score + len(wr)*rook_score + len(wq)*queen_score + len(wk)*king_score
    white_score += sum([pawn_board[p] for p in wp])
    white_score += sum([knight_board[p] for p in wn])
    white_score += sum([bishop_board[p] for p in wb])
    white_score += sum([rook_board[p] for p in wr])
    white_score += sum([queen_board[p] for p in wq])

    black_score += len(bp)*pawn_score + len(bn)*knight_score + len(bb)*bishop_score + len(br)*rook_score + len(bq)*queen_score + len(bk)*king_score
    black_score += sum([pawn_board[chess.square_mirror(p)] for p in bp])
    black_score += sum([knight_board[chess.square_mirror(p)] for p in bn])
    black_score += sum([bishop_board[chess.square_mirror(p)] for p in bb])
    black_score += sum([rook_board[chess.square_mirror(p)] for p in br])
    black_score += sum([queen_board[chess.square_mirror(p)] for p in bq])

    endgame = False

    if len(wq) == 0 and len(bq) == 0:
        endgame = True
    elif len(wq) == 0 and (len(bq) == 1 and (len(bb)+len(bn)) <= 1 and len(br) == 0):
        endgame = True
    elif len(bq) == 0 and (len(wq) == 1 and (len(wb)+len(wn)) <= 1 and len(wr) == 0):
        endgame = True
    elif (len(bq) == 1 and (len(bb)+len(bn)) <= 1 and len(br) == 0) and (len(wq) == 1 and (len(wb)+len(wn)) <= 1 and len(wr) == 0):
        endgame = True

    if endgame:
        white_score += sum([king_board_end[p] for p in wk])
        black_score += sum([king_board_end[chess.square_mirror(p)] for p in bk])
    else:
        white_score += sum([king_board_middle[p] for p in wk])
        black_score += sum([king_board_middle[chess.square_mirror(p)] for p in bk])

    if len(wb) > 1:
        white_score += bishop_pair

    if len(bb) > 1:
        black_score += bishop_pair

    white_score += len(wn) * knight_adj[len(wp)]
    white_score += len(wr) * rook_adj[len(wp)]

    black_score += len(bn) * knight_adj[len(bp)]
    black_score += len(br) * rook_adj[len(bp)]

    if maxi_is_white:
        return white_score-black_score
    else:
        return black_score-white_score


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
    player_color = chess.WHITE

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
