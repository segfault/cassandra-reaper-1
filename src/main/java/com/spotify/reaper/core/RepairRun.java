/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spotify.reaper.core;

import org.joda.time.DateTime;

public class RepairRun {

  private final long id;

  // IDEA: maybe we want to have start and stop token for parallel runners on same repair run?
  //private final long startToken;
  //private final long endToken;

  private final String cause;
  private final String owner;
  private final String clusterName;
  private final long columnFamilyId;
  private final RunState runState;
  private final DateTime creationTime;
  private final DateTime startTime;
  private final DateTime endTime;
  private final double intensity;

  public long getId() {
    return id;
  }

  public long getColumnFamilyId() {
    return columnFamilyId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public String getCause() {
    return cause;
  }

  public String getOwner() {
    return owner;
  }

  public RunState getRunState() {
    return runState;
  }

  public DateTime getCreationTime() {
    return creationTime;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public DateTime getEndTime() {
    return endTime;
  }

  public double getIntensity() {
    return intensity;
  }

  public static RepairRun getCopy(RepairRun repairRun, RunState newState,
                                  DateTime startTime, DateTime endTime) {
    return new RepairRun.Builder(repairRun.getClusterName(), repairRun.getColumnFamilyId(),
                                 newState, repairRun.getCreationTime(), repairRun.getIntensity())
        .cause(repairRun.getCause())
        .owner(repairRun.getOwner())
        .startTime(startTime)
        .endTime(endTime)
        .build(repairRun.getId());
  }

  public enum RunState {
    NOT_STARTED,
    RUNNING,
    ERROR,
    DONE,
    PAUSED
  }

  private RepairRun(Builder builder, long id) {
    this.id = id;
    this.clusterName = builder.clusterName;
    this.columnFamilyId = builder.columnFamilyId;
    this.cause = builder.cause;
    this.owner = builder.owner;
    this.runState = builder.runState;
    this.creationTime = builder.creationTime;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
    this.intensity = builder.intensity;
  }

  public static class Builder {

    public final String clusterName;
    public final long columnFamilyId;
    public final RunState runState;
    public final DateTime creationTime;
    public final double intensity;
    private String cause;
    private String owner;
    private DateTime startTime;
    private DateTime endTime;

    public Builder(String clusterName, long columnFamilyId, RunState runState,
                   DateTime creationTime, double intensity) {
      this.clusterName = clusterName;
      this.columnFamilyId = columnFamilyId;
      this.runState = runState;
      this.creationTime = creationTime;
      this.intensity = intensity;
    }

    public Builder cause(String cause) {
      this.cause = cause;
      return this;
    }

    public Builder owner(String owner) {
      this.owner = owner;
      return this;
    }

    public Builder startTime(DateTime startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(DateTime endTime) {
      this.endTime = endTime;
      return this;
    }

    public RepairRun build(long id) {
      return new RepairRun(this, id);
    }
  }
}
