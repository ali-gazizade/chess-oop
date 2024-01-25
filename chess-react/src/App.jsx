import './App.css';
import { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
  const [columns] = useState(['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']);
  const [rows] = useState((new Array(8).fill(0)).map((e, i) => (i + 1)));
  const [boardSquares, setBoardSquares] = useState([]);

  useEffect(() => {
    (async () => {
      const gameRes = await axios.get('/bi/game/data');
      setBoardSquares(gameRes?.data?.squares);
    })();
  }, []);

  const selectSquare = async (column, row) => {
    const gameRes = await axios.post(`/bi/game/select?column=${column}&row=${row}`);
    setBoardSquares(gameRes?.data?.squares);
  };

  return (
    <div className="chessboard">{
      (columns && rows && boardSquares?.length)
        ? [...rows].reverse().map((row, rowI) => (
          columns.map((column, columnI) => {
            const square = boardSquares.find(e => e.row == row && e.column == column);

            if (!square) {
              console.log(`${column} ${row} has no square`);
            }

            return < div
              key={column + row}
              className={(rowI + columnI) % 2 === 0 ? "square even" : "square odd"}
              onClick={() => selectSquare(column, row)}
            >
              { square.piece
                ? <span className="piece">{
                  square.pieceColor === 'White'
                    ? (
                      square.piece === 'Pawn'
                        ? <>&#9817;</>
                        : square.piece === 'Knight'
                          ? <>&#9816;</>
                          : square.piece === 'Bishop'
                            ? <>&#9815;</>
                            : square.piece === 'Rook'
                              ? <>&#9814;</>
                              : square.piece === 'Queen'
                                ? <>&#9813;</>
                                : square.piece === 'King'
                                  ? <>&#9812;</>
                                  : null
                    )
                    : (
                      square.piece === 'Pawn'
                        ? <>&#9823;</>
                        : square.piece === 'Knight'
                          ? <>&#9822;</>
                          : square.piece === 'Bishop'
                            ? <>&#9821;</>
                            : square.piece === 'Rook'
                              ? <>&#9820;</>
                              : square.piece === 'Queen'
                                ? <>&#9819;</>
                                : square.piece === 'King'
                                  ? <>&#9818;</>
                                  : null
                    )
                }</span>
                : null
              }
              {
                square.state
                ? <div className={'squareState ' + (
                    square.state === 'CAN_BE_MOVED_TO'
                      ? 'canBeMovedTo'
                      : square.state === 'PIECE_CAN_BE_TAKEN'
                        ? 'pieceCanBeTaken'
                        : 'otherState'
                )}></div>
                : null
              }
            </div>
          })
        ))
        : null
    }</div>
  );
}

export default App;
