/*
 * Copyright 2023 Mark.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mf;

import java.util.LinkedList;

public class Checkpoints {
    // now type‐safe & uses diamond
    private final LinkedList<Pair<String,Long>> POINTS = new LinkedList<>();

    public Checkpoints() { }

    public void addCheckPoint(String name, long point) {
        POINTS.addLast(Pair.of(name, point));
    }

    public void verifyCheckPoint(String name, long point) {
        if (POINTS.isEmpty()) {
            throw new IllegalStateException(
                String.format("Unmatched checkpoint (%s : %d)", name, point));
        }

        Pair<String,Long> checkpoint = POINTS.removeFirst();
        if (point != checkpoint.B) {
            throw new IllegalStateException(
                String.format(
                  "Checkpoint doesn't match (%s : %d) != (%s : %d)",
                  checkpoint.A, checkpoint.B, name, point));
        }
    }
}
